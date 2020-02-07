public abstract class ComplexFunction {

    public abstract ComplexNumber func(ComplexNumber num);

    public ComplexNumber call(ComplexNumber num) {
        return func(num.clone());
    }

    public double callMagnitude(ComplexNumber num) {
        return call(num).magnitude();
    }
}