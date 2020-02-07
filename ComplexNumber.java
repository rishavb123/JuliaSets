public class ComplexNumber {

    private double real;
    private double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber add(ComplexNumber num) {
        real += num.getReal();
        imaginary += num.getImaginary();
        return this;
    }

    public ComplexNumber subtract(ComplexNumber num) {
        real -= num.getReal();
        imaginary -= num.getImaginary();
        return this;
    }

    public ComplexNumber multiply(ComplexNumber num) {
        double temp = real * num.getReal() - imaginary * num.getImaginary();
        imaginary = real * num.getImaginary() + imaginary * num.getReal();
        real = temp;
        return this;
    }

    public ComplexNumber pow(int power) {
        ComplexNumber z = new ComplexNumber(1, 0);
        for(int i = 0; i < power; i++)
            z.multiply(this);
        for(int i = 0; i > power; i--)
            z.divide(this);
        return z;
    }

    public ComplexNumber divide(ComplexNumber num) {
        return multiply(num.reciprocal());
    }

    public ComplexNumber scale(double num) {
        real *= num;
        imaginary *= num;
        return this;
    }

    /*
        1/(a + bi) = (a - bi)/(a - bi) * 1/(a + bi) = (a - bi) / (a * a + b * b)
    */
    public ComplexNumber reciprocal() {
        return conjugate().scale(1 / squaredMagnitude());
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(real, -imaginary);
    }

    public double squaredMagnitude() {
        return real * real + imaginary * imaginary;
    }

    public double magnitude() {
        return Math.sqrt(squaredMagnitude());
    }

    public ComplexNumber clone() {
        return new ComplexNumber(real, imaginary);
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }
   
    public double getImaginary() {
        return imaginary;
    }

    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    public String toString() {
        return real + " + i * " + imaginary;
    }

}