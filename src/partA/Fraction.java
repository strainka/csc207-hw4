package partA;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Fraction
{

  // +------------------+---------------------------------------------
  // | Design Decisions |
  // +------------------+
  /*
   * A fraction need to contain a pair of integers: numerator and denuminator
   * 
   * The numerator carries the sign of the fraction and can be positive,
   * nagative, and zero. But denuminator are always positive.
   * 
   * Fractions with the same value may have different represatations, we need to
   * make all fractions reduced to their lowest terms. Warning: The canonical
   * representation of 0 as a fraction is 0/1.
   */

  private static BigInteger NEGATIVE_ONE = BigInteger.valueOf (-1);
  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+

  // The numerator of fraction can be positve, nagative, and zero
  BigInteger numerator;

  // The denominator of fraction must be positve
  BigInteger denominator;

  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+

  public Fraction (BigInteger numerator, BigInteger denominator)
                                                                throws Exception
  {
    if (denominator.intValue () == 0)
      throw new Exception ("Zero is illegal denominator");
    this.numerator = numerator;
    this.denominator = denominator;
    this.cleanup ();
  }// Fraction(BigInteger, BigInteger)

  public Fraction (BigInteger numertor)
  {
    this.numerator = numerator;
    this.denominator = BigInteger.valueOf (1);
  }// Fraction(BigInteger)

  public Fraction (int numerator, int denominator) throws Exception
  {
    if (denominator == 0)
      throw new Exception ("Zero is illegal denominator");
    this.numerator = BigInteger.valueOf (numerator);
    this.denominator = BigInteger.valueOf (denominator);
    this.cleanup ();
  }// Fraction(int,int)

  public Fraction (int numerator)
  {
    this.numerator = BigInteger.valueOf (numerator);
    this.denominator = BigInteger.valueOf (1);

  }// Fraction(int)

  public Fraction (String fract) throws Exception
  {
    String[] parts = fract.split ("/");
    if (parts.length > 2)
      throw new Exception ("Too many values");
    else if ((parts.length != 1) && (Long.parseLong (parts[1]) == 0))
      throw new Exception ("Zero is illegal denominator.");
    try
      {
        this.numerator = BigInteger.valueOf (Long.parseLong (parts[0]));
        if (parts.length == 1)
          this.denominator = BigInteger.valueOf (1);
        else
          this.denominator = BigInteger.valueOf (Long.parseLong (parts[1]));
      }
    catch (Exception e)
      {
        throw new Exception ("Problem with " + fract + ": not a fraction");
      }

    this.cleanup ();

  }// Fraction(String)

  public Fraction (long numerator, long denominator) throws Exception
  {
    if (denominator == 0)
      throw new Exception ("Zero is illegal denominator");
    this.numerator = BigInteger.valueOf (numerator);
    this.denominator = BigInteger.valueOf (denominator);
    this.cleanup ();
  }// Fraction(long,long)

  public Fraction (double number)
  {
    double mantissa = (number % 1);
    if (mantissa == 0)
      {
        this.numerator = BigInteger.valueOf ((long) number);
        this.denominator = BigInteger.valueOf (1);
      }
    else
      {
        long multiplier = (long) (1 / mantissa);
        long nearInt = Math.round (multiplier * number);
        this.numerator = BigInteger.valueOf (nearInt);
        this.denominator = BigInteger.valueOf (multiplier);
      }

  }// Fraction(double)

  // +---------------------------+------------------------------------------
  // | Standard Object Methods |
  // +--------------------------+

  /*
   * convert the form to Fraction to display.
   */
  public String
    toString ()
  {
    return this.numerator + "/" + this.denominator;
  }// toString()

  /*
   * compare two fraction, return 1 if the first one is greater than the
   * second.else return 0
   */
  public int
    compareTo (Fraction other)
  {
    return this.numerator.multiply (other.denominator)
                         .compareTo (this.denominator.multiply (other.numerator));
  }// compareTo(Fraction)

  /*
   * Determine if this fraction and other fraction are equal.
   */
  public boolean
    equals (Fraction other)
  {
    this.simplify();
    other.simplify();
    return (this.numerator.equals (other.numerator))
           && (this.denominator.equals (other.denominator));
  }// equals(Fraction)

  /*
   * Determine if two object of fractions are equal.
   */
  public boolean
    equals (Object other)
  {
    return (other instanceof Fraction) && (this.equals ((Fraction) other));
  }// equals(Object)

  // +-----------------+----------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * If the fraction is negative, make sure the numerator is negative and the
   * denominator is positive. If both the denominator and numerator are
   * negative, then make them both positive. The function then converts the
   * fraction to its simplest possible form
   */

  private void
    cleanup ()
  {
    if (this.denominator.signum () < 0)
      {
        this.denominator = this.denominator.abs ();
        if (this.numerator.signum () > 0)
          {
            this.numerator = this.numerator.multiply (NEGATIVE_ONE);
          }
        else
          {
            this.numerator = this.numerator.abs ();
          }
      }
    this.simplify ();
  }

  private void
    simplify ()
  {
    BigInteger gcd = this.numerator.gcd (this.denominator);
    this.numerator = this.numerator.divide (gcd);
    this.denominator = this.denominator.divide (gcd);
  } // simplify()

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Add another fraction to this fraction.
   */
  public Fraction
    add (Fraction addend)
      throws Exception
  {
    BigInteger resultNumerator;
    BigInteger resultDenominator;

    // The denominator of the result is the
    // product of this object's denominator
    // and addMe's denominator
    resultDenominator = this.denominator.multiply (addend.denominator);
    // The numerator is more complicated
    resultNumerator = (this.numerator.multiply (addend.denominator)).add (addend.numerator.multiply (this.denominator));

    // Return the computed value
    Fraction resultFrac = new Fraction (resultNumerator, resultDenominator);
    resultFrac.simplify ();
    return resultFrac;

  } // add(Fraction)

  /**
   * Convert this fraction to a double
   */
  public double
    toDouble ()
  {
    return this.numerator.doubleValue () / this.denominator.doubleValue ();

  }// toDouble()

  /*
   * convert this fraction to a Bigdouble
   */
  public BigDecimal
    toBigDecimal ()
  {
    BigDecimal bigNumerator = new BigDecimal (this.numerator);
    BigDecimal bigDenominator = new BigDecimal (this.denominator);
    return bigNumerator.divide (bigDenominator);
  }// toBigDecimal()

  /*
   * Multiply this fraction with another fraction.
   */
  public Fraction
    multiplyBy (Fraction multiplier)
  {
    BigInteger resultNumerator = this.numerator.multiply (multiplier.numerator);
    BigInteger resultDenominator = this.denominator.multiply (multiplier.denominator);

    try
      {
        Fraction resultFraction = new Fraction (resultNumerator,
                                                resultDenominator);
        return resultFraction;
      }
    catch (Exception reason)
      {
        return new Fraction (-1);
      }
  }// multiplyBy(Fraction)

  /*
   * Divide this fraction by another
   */
  public Fraction
    divideBy (Fraction divisor)
  {
    BigInteger resultNumerator = this.numerator.multiply (divisor.denominator);
    BigInteger resultDenominator = this.denominator.multiply (divisor.numerator);
    try
      {
        Fraction resultFraction = new Fraction (resultNumerator,
                                                resultDenominator);
        return resultFraction;
      }
    catch (Exception reason)
      {
        return new Fraction (-1);
      }
  }// divideBy(Fraction)

  public Fraction
    negate ()
      throws Exception
  {
    BigInteger newmerator = this.numerator.multiply (NEGATIVE_ONE);
    return new Fraction (newmerator, this.denominator);
  }// negate()
}// public class Fraction