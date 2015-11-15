/**
 * 
 */

/* Connectives */
var NEG  = '~'
var CONJ = '*'
var DISJ = '|'
var IMPL = '>'
var EQV  = '='

/* Truth values */
var T = true
var F = false
	
/* Formula class */
function Formula(f) {
	this.con = null
	this.sf1 = null
	this.sf2 = null
	this.literal = stripBrackets(f)
	this.analyse(this.literal)
}

Formula.prototype.analyse = function(f) {
	var nesting = 0;
	var len = f.length;
	if (len === 1)
		// An atomic formula. Nothing to scan.
		return;
	for (var i = 0; i < len; i++) {
		var c = f.charAt(i);
		if (c === '(') {
			nesting++;
		} else if (c === ')') {
			nesting--;
		} else if (nesting === 0) {	
			// We are on the highest nesting level. Check for main connective.
			if (c === CONJ || c === DISJ || c === IMPL || c === EQV) {
				// Binary connective found. Create 2 sub-formulas.
				this.con = c;
				this.sf1 = new Formula(f.slice(0, i));
				this.sf2 = new Formula(f.slice(i + 1));
				return
			} else if (c === NEG) {
				// Unary connective found. Create 1 sub-formula.
				this.con = c;
				this.sf1 = new Formula(f.slice(i + 1));
				// We don't return here since a binary connective might be found
				// later and, if so, that connective would be the main one.
			}
		}
	}
}

/**
 * Assigns truth values to this formula and returns the resulting truth value.
 * Expects assignment in dictionary format.
 */
Formula.prototype.assign = function(assignment) {
	if (!this.con) {
		// An atomic formula.
		return assignment[this.literal]
	}
	switch (this.con) {
	    case NEG:
	        return !this.sf1.assign(assignment);
	    case CONJ:
	        return this.sf1.assign(assignment) && this.sf2.assign(assignment);
	    case DISJ:
	        return this.sf1.assign(assignment) || this.sf2.assign(assignment);
	    case IMPL:
	        return !this.sf1.assign(assignment) || this.sf2.assign(assignment);
	    case EQV:
	        return this.sf1.assign(assignment) == this.sf2.assign(assignment);
	}
}

Formula.prototype.vars = function() {
	if (!this.con) {
		// Atomic formula.
		return [this.literal];
	}
	if (this.con === NEG) {
		// Unary compound formula, vars are those of sub-formula.
		return this.sf1.vars();
	}
	// Binary compound formula, uniquely merge vars of sub-formulas.
	return uniqueMerge(this.sf1.vars(), this.sf2.vars());
}

Formula.prototype.str = function() {
	if (this.con === NEG) {
		return 'literal = ' + this.literal + '\ncon = ' + this.con + '\nsf1 = {' + this.sf1.literal + '}'
	}
	return 'literal = ' + this.literal + '\ncon = ' + this.con + '\nsf1 = {' + this.sf1.literal + '}\nsf2 = {' + this.sf2.literal + '}' 
}

function stripBrackets(f) {
	if (f.charAt(0) === '(') {
		// Openning bracket found. Look for closure.
		var len = f.length;
		var nesting = 0;
		for (var i = 1; i < len; i++) {
			var c = f.charAt(i);
			if (c === ')') {
				if (nesting === 0) {
					// Closure found. Only strip if it's the last char.
					if (i === len - 1) {
						return f.slice(1, len - 1);
					} else {
						return f;
					}
				} else {
					nesting--;
				}
			} else if (c === '(') {
				nesting++;
			}
		}
	}
	return f;
}

function uniqueMerge(a1, a2) {
	var both = a1.concat(a2);
	var newarray = [];
	var len = both.length;
    for (var i = 0; i < len; i++) {
    	if (!contains(newarray, both[i])) {
    		newarray.push(both[i])
		}
    }
    return newarray;
}

function contains(a, obj) {
    for (var i = 0; i < a.length; i++) {
        if (a[i] === obj) 
            return true;
    }
    return false;
}
