/*
 * www.javagl.de - Geom - Geometry utilities
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.geom;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Utility methods related to affine transforms
 */
public class AffineTransforms
{
    /**
     * Invert the given affine transform, storing the result in the
     * given destination. If the given destination is <code>null</code>,
     * then a new affine transform will be created and returned.
     * 
     * @param atSrc The source
     * @param atDst The destination
     * @return The destination.
     * @throws IllegalArgumentException If the given transform can not
     * be inverted.
     */
    public static AffineTransform invert(
        AffineTransform atSrc, AffineTransform atDst)
    {
        try
        {
            if (atDst == null)
            {
                return atSrc.createInverse();
            }
            atDst.setTransform(atSrc);
            atDst.invert();
            return atDst;
        }
        catch (NoninvertibleTransformException e)
        {
            throw new IllegalArgumentException("Non-invertible transform", e);
        }
    }
    
    
    /**
     * Creates a function that returns an affine transform that performs
     * a scaling according to values returned by the given functions.
     * 
     * @param <T> The function argument type
     * 
     * @param xScalingFunction The function that returns the x-scaling
     * @param yScalingFunction The function that returns the y-scaling
     * @return The function
     */
    public static <T> Function<T, AffineTransform> scaling(
        final ToDoubleFunction<? super T> xScalingFunction,
        final ToDoubleFunction<? super T> yScalingFunction)
    {
        return new Function<T, AffineTransform>()
        {
            @Override
            public AffineTransform apply(T t)
            {
                double x = xScalingFunction.applyAsDouble(t);
                double y = yScalingFunction.applyAsDouble(t);
                return AffineTransform.getScaleInstance(x, y);
            }
        };
    }

    /**
     * Creates a function that returns an affine transform that performs
     * a scaling according to value returned by the given function.
     * 
     * @param <T> The function argument type
     * 
     * @param scalingFunction The function that returns the scaling
     * @return The function
     */
    public static <T> Function<T, AffineTransform> scaling(
        final ToDoubleFunction<? super T> scalingFunction)
    {
        return new Function<T, AffineTransform>()
        {
            @Override
            public AffineTransform apply(T t)
            {
                double s = scalingFunction.applyAsDouble(t);
                return AffineTransform.getScaleInstance(s, s);
            }
        };
    }

    /**
     * Creates a function that returns an affine transform that performs
     * a rotation according to value returned by the given function.
     * 
     * @param <T> The function argument type
     * 
     * @param angleRadFunction The function that returns the rotation angle,
     * in radians
     * @return The function
     */
    public static <T> Function<T, AffineTransform> rotating(
        final ToDoubleFunction<? super T> angleRadFunction)
    {
        return new Function<T, AffineTransform>()
        {
            @Override
            public AffineTransform apply(T t)
            {
                double angleRad = angleRadFunction.applyAsDouble(t);
                return AffineTransform.getRotateInstance(angleRad);
            }
        };
    }

    /**
     * Creates a function that returns an affine transform that performs
     * a rotation according to value returned by the given function.
     * 
     * @param <T> The function argument type
     * 
     * @param angleRadFunction The function that returns the rotation angle,
     * in radians
     * @param anchorX The x-coordinate of the rotation center
     * @param anchorY The y-coordinate of the rotation center
     * @return The function
     */
    public static <T> Function<T, AffineTransform> rotating(
        final ToDoubleFunction<? super T> angleRadFunction,
        final double anchorX, final double anchorY)
    {
        return new Function<T, AffineTransform>()
        {
            @Override
            public AffineTransform apply(T t)
            {
                double angleRad = angleRadFunction.applyAsDouble(t);
                return AffineTransform.getRotateInstance(
                    angleRad, anchorX, anchorY);
            }
        };
    }
    
    /**
     * Creates a function that returns an affine transform that performs
     * a translation according to values returned by the given functions.
     * 
     * @param <T> The function argument type
     * 
     * @param xTranslationFunction The function that returns the x-translation
     * @param yTranslationFunction The function that returns the y-translation
     * @return The function
     */
    public static <T> Function<T, AffineTransform> translate(
        final ToDoubleFunction<? super T> xTranslationFunction,
        final ToDoubleFunction<? super T> yTranslationFunction)
    {
        
        return new Function<T, AffineTransform>()
        {
            @Override
            public AffineTransform apply(T t)
            {
                double x = xTranslationFunction.applyAsDouble(t);
                double y = yTranslationFunction.applyAsDouble(t);
                return AffineTransform.getTranslateInstance(x, y);
            }
        };
    }

    /**
     * Creates a function that returns an affine transform that is the
     * concatenation of the affine transforms returned by the given 
     * functions. If either of the functions returns <code>null</code>,
     * then the result will be equal to the affine transform of the 
     * other function. If both functions return <code>null</code>, then
     * the function will also return <code>null</code>.  
     * 
     * @param <T> The function argument type
     * 
     * @param f0 The first function
     * @param f1 The second function
     * @return The function
     */
    public static <T> Function<T, AffineTransform> compose(
        final Function<? super T, ? extends AffineTransform> f0, 
        final Function<? super T, ? extends AffineTransform> f1)
    {
        return new Function<T, AffineTransform>()
        {
            @Override
            public AffineTransform apply(T t)
            {
                AffineTransform at0 = f0.apply(t);
                AffineTransform at1 = f1.apply(t);
                if (at0 == null)
                {
                    if (at1 == null)
                    {
                        return null;
                    }
                }
                if (at1 == null)
                {
                    return new AffineTransform(at1);
                }
                AffineTransform at = new AffineTransform(at0);
                at.concatenate(at1);
                return at;
            }
        };
    }
    
    
    /**
     * Computes the distance that two points have when they are transformed
     * with the given affine transform, and initially had the given distance
     * in x-direction
     * 
     * @param at The affine transform
     * @param distanceX The distance
     * @return The distance after the transform
     */
    public static double computeDistanceX(AffineTransform at, double distanceX)
    {
        double dx = at.getScaleX() * distanceX;
        double dy = at.getShearY() * distanceX;
        double result = Math.sqrt(dx*dx+dy*dy);
        return result;
    }

    /**
     * Computes the distance that two points have when they are transformed
     * with the given affine transform, and initially had the given distance
     * in y-direction
     * 
     * @param at The affine transform
     * @param distanceY The distance
     * @return The distance after the transform
     */
    public static double computeDistanceY(AffineTransform at, double distanceY)
    {
        double dx = at.getShearX() * distanceY;
        double dy = at.getScaleY() * distanceY;
        double result = Math.sqrt(dx*dx+dy*dy);
        return result;
    }
    
    /**
     * Computes the x-coordinate of the given point when it is transformed
     * with the given affine transform
     * 
     * @param at The affine transform
     * @param p The point
     * @return The x-coordinate of the transformed point
     */
    public static double computeX(AffineTransform at, Point2D p)
    {
        return computeX(at, p.getX(), p.getY());
    }

    /**
     * Computes the x-coordinate of the specified point when it is transformed
     * with the given affine transform
     * 
     * @param at The affine transform
     * @param x The x-coordinate of the point
     * @param y The y-coordinate of the point
     * @return The x-coordinate of the transformed point
     */
    public static double computeX(AffineTransform at, double x, double y)
    {
        return 
            at.getScaleX() * x + 
            at.getShearX() * y + 
            at.getTranslateX();
    }
    
    /**
     * Computes the y-coordinate of the given point when it is transformed
     * with the given affine transform
     * 
     * @param at The affine transform
     * @param p The point
     * @return The y-coordinate of the transformed point
     */
    public static double computeY(AffineTransform at, Point2D p)
    {
        return computeY(at, p.getX(), p.getY());
    }

    /**
     * Computes the y-coordinate of the specified point when it is transformed
     * with the given affine transform
     * 
     * @param at The affine transform
     * @param x The x-coordinate of the point
     * @param y The y-coordinate of the point
     * @return The y-coordinate of the transformed point
     */
    public static double computeY(AffineTransform at, double x, double y)
    {
        return 
            at.getShearY() * x + 
            at.getScaleY() * y + 
            at.getTranslateY();
    }
    
    
    /**
     * An implementation of the 
     * <code>AffineTransform#createTransformedShape</code> method
     * that is optimized for certain simple shapes (like 
     * <code>Rectangle2D</code> and <code>Line2D</code>): If the given 
     * transform only consists of translation (or translation and 
     * scaling) then a new shape will be constructed directly, 
     * avoiding the general (but inefficient) method of creating
     * a <code>Path2D</code>.
     * Otherwise, <code>AffineTransform#createTransformedShape</code>
     * will be called
     * 
     * @param at The affine transform
     * @param shape The shape
     * @return The transformed shape
     */
    public static Shape createTransformedShape(AffineTransform at, Shape shape)
    {
        if (shape instanceof Rectangle2D)
        {
            Rectangle2D r = (Rectangle2D)shape;
            if (!hasRotation(at))
            {
                if (!hasScale(at))
                {
                    return createTranslated(at, r);
                }
                return createScaledAndTranslated(at, r);
            }
        }
        else if (shape instanceof Line2D)
        {
            Line2D l = (Line2D)shape;
            if (!hasRotation(at))
            {
                if (!hasScale(at))
                {
                    return createTranslated(at, l);
                }
                return createScaledAndTranslated(at, l);
            }
        }
        return at.createTransformedShape(shape);
    }
    
    /**
     * Creates a new rectangle that is transformed with the given transform,
     * which is assumed to only contain a translation
     * 
     * @param at The affine transform
     * @param r The rectangle
     * @return The transformed rectangle
     */
    private static Rectangle2D createTranslated(
        AffineTransform at, Rectangle2D r)
    {
        Rectangle2D result = new Rectangle2D.Double(
            r.getX() + at.getTranslateX(), 
            r.getY() + at.getTranslateY(),
            r.getWidth(), r.getHeight());
        return result;
    }

    /**
     * Creates a new rectangle that is transformed with the given transform,
     * which is assumed to only contain a scale (including flips) and 
     * translation
     * 
     * @param at The affine transform
     * @param r The rectangle
     * @return The transformed rectangle
     */
    private static Rectangle2D createScaledAndTranslated(
        AffineTransform at, Rectangle2D r)
    {
        double x0 = r.getX() * at.getScaleX() + at.getTranslateX();
        double y0 = r.getY() * at.getScaleY() + at.getTranslateY();
        double x1 = x0 + r.getWidth() * at.getScaleX();
        double y1 = y0 + r.getHeight() * at.getScaleY();
        double minX = Math.min(x0, x1);
        double minY = Math.min(y0, y1);
        double maxX = Math.max(x0, x1);
        double maxY = Math.max(y0, y1);
        Rectangle2D result =
            new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
        return result;
    }
    
    /**
     * Creates a new line that is transformed with the given transform,
     * which is assumed to only contain a translation
     * 
     * @param at The affine transform
     * @param l The line
     * @return The transformed line
     */
    private static Line2D createTranslated(
        AffineTransform at, Line2D l)
    {
        Line2D result = new Line2D.Double(
            l.getX1() + at.getTranslateX(), 
            l.getY1() + at.getTranslateY(),
            l.getX2() + at.getTranslateX(), 
            l.getY2() + at.getTranslateY());
        return result;
    }

    /**
     * Creates a new line that is transformed with the given transform,
     * which is assumed to only contain a scale and translation
     * 
     * @param at The affine transform
     * @param l The line
     * @return The transformed line
     */
    private static Line2D createScaledAndTranslated(
        AffineTransform at, Line2D l)
    {
        Line2D result = new Line2D.Double(
            l.getX1() * at.getScaleX() + at.getTranslateX(), 
            l.getY1() * at.getScaleY() + at.getTranslateY(),
            l.getX2() * at.getScaleX() + at.getTranslateX(), 
            l.getY2() * at.getScaleY() + at.getTranslateY());
        return result;
    }
    
    
    
    /**
     * Returns whether the given affine transform has a rotation component
     * 
     * @param at The affine transform
     * @return Whether the transform has a rotation component
     */
    private static boolean hasRotation(AffineTransform at)
    {
        int type = at.getType();
        if (type == AffineTransform.TYPE_GENERAL_TRANSFORM)
        {
            return true;
        }
        return 
            (((type & AffineTransform.TYPE_GENERAL_ROTATION ) != 0) ||
             ((type & AffineTransform.TYPE_QUADRANT_ROTATION) != 0));
    }
    
    
    /**
     * Returns whether the given affine transform has a scale component.
     * This includes flipping (i.e. scaling about -1.0 in x- or y-direction).
     * 
     * @param at The affine transform
     * @return Whether the transform has a scale component
     */
    private static boolean hasScale(AffineTransform at)
    {
        int type = at.getType();
        if (type == AffineTransform.TYPE_GENERAL_TRANSFORM)
        {
            return true;
        }
        return 
            (((type & AffineTransform.TYPE_UNIFORM_SCALE ) != 0) ||
             ((type & AffineTransform.TYPE_GENERAL_SCALE ) != 0) ||
             ((type & AffineTransform.TYPE_FLIP          ) != 0));
    }
    
    /**
     * Sets the given result to be an affine transform that transforms the 
     * unit square into the given rectangle. If the given result is 
     * <code>null</code>, then a new transform will be created and returned.
     * 
     * @param rectangle The rectangle
     * @param result The result
     * @return The result
     */
    public static AffineTransform getScaleInstance(
        Rectangle2D rectangle, AffineTransform result)
    {
        return getScaleInstance(
            rectangle.getMinX(), 
            rectangle.getMinY(), 
            rectangle.getMaxX(), 
            rectangle.getMaxY(), 
            result);
    }
    
    /**
     * Sets the given result to be an affine transform that transforms the 
     * unit square into the given rectangle. If the given result is 
     * <code>null</code>, then a new transform will be created and returned.
     * 
     * @param minX The minimum x-coordinate
     * @param minY The minimum y-coordinate
     * @param maxX The maximum x-coordinate
     * @param maxY The maximum y-coordinate
     * @param result The result
     * @return The result
     */
    public static AffineTransform getScaleInstance(
        double minX, double minY, double maxX, double maxY, 
        AffineTransform result)
    {
        AffineTransform localResult = result;
        if (result == null)
        {
            localResult = AffineTransform.getTranslateInstance(minX, minY);
        }
        else
        {
            localResult.setToTranslation(minX, minY);
        }
        localResult.scale(maxX - minX, maxY - minY);
        return localResult;
    }
    
    
    
    /**
     * Creates a formatted, multi-line string representation of the
     * given affine transform
     * 
     * @param at The affine transform
     * @return The string
     */
    public static String toString(AffineTransform at)
    {
        return toString(at, "%f");
    }
    
    /**
     * Creates a formatted, multi-line string representation of the
     * given affine transform, using the given format for each entry
     * 
     * @param at The affine transform
     * @param format The format
     * @return The string
     */
    public static String toString(AffineTransform at, String format)
    {
        return toString(at, Locale.getDefault(), format);
    }
    
    /**
     * Creates a formatted, multi-line string representation of the
     * given affine transform, using the given format for each entry
     * 
     * @param at The affine transform
     * @param locale The locale
     * @param format The format
     * @return The string
     */
    public static String toString(
        AffineTransform at, Locale locale, String format)
    {
        int cellSize = 10;
        String s00 = String.format(locale, format, at.getScaleX());
        String s01 = String.format(locale, format, at.getShearX());
        String s02 = String.format(locale, format, at.getTranslateX());
        String s10 = String.format(locale, format, at.getShearY());
        String s11 = String.format(locale, format, at.getScaleY());
        String s12 = String.format(locale, format, at.getTranslateY());
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%"+cellSize+"s", s00)).append(" ");
        sb.append(String.format("%"+cellSize+"s", s01)).append(" ");
        sb.append(String.format("%"+cellSize+"s", s02)).append("\n");
        sb.append(String.format("%"+cellSize+"s", s10)).append(" ");
        sb.append(String.format("%"+cellSize+"s", s11)).append(" ");
        sb.append(String.format("%"+cellSize+"s", s12)).append(" ");
        return sb.toString();
    }

    /**
     * Private constructor to prevent instantiation
     */
    private AffineTransforms()
    {
        // Private constructor to prevent instantiation
    }
    
}
