public abstract class ComplexFunction {

    public static final ComplexNumber h = new ComplexNumber(0.000001, 0);

    public abstract ComplexNumber func(ComplexNumber num);

    public ComplexNumber call(ComplexNumber num) {
        return func(num.clone());
    }

    public double callMagnitude(ComplexNumber num) {
        return call(num).magnitude();
    }

    public ComplexFunction derivative() {
        ComplexFunction f = this;
        return new ComplexFunction() {

			@Override
			public ComplexNumber func(ComplexNumber num) {
				return (f.call(num).add(f.call(num.add(h)))).divide(h);
			}
            
        };
    }
    
    public ComplexFunction newtonsMethod() {
        ComplexFunction f = this;
        ComplexFunction derivativeFunction = derivative(); 
        return new ComplexFunction() {

			@Override
			public ComplexNumber func(ComplexNumber num) {
				return num.subtract(f.call(num).divide(derivativeFunction.call(num)));
			}

        };
    }

}