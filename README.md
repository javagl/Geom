# Geom
Utility classes for geometry computations

This project contains utility classes for geometry computations, mainly 
targeting the classes from the <code>java.awt.geom</code> package. 
For most of the classes and interfaces of the <code>java.awt.geom</code>
package, there is a corresponding class that contains utility methods:
<ul>
  <li>java.awt.geom.Point2D : de.javagl.geom.Points</li>
  <li>java.awt.geom.Line2D : de.javagl.geom.Liness</li>
  <li>java.awt.geom.Rectangle2D : de.javagl.geom.Rectangles</li>
  <li>java.awt.geom.Path2D : de.javagl.geom.Paths</li>
  <li>java.awt.geom.Shape : de.javagl.geom.Shapes</li>
  <li>java.awt.geom.AffineTransform : de.javagl.geom.AffineTransforms</li>
</ul>
For the primitives, these classes contain utility methods that in
general allow modifications of the primitives that are implemented
aiming at high performance and low memory overhead. For example,
most of the methods have an <i>optional</i> parameter that may 
store the transformed result, and will return this result parameter,
or create a new object internally when the result parameter 
was <code>null</code>.<br>
<br>
The remaining classes deal with tasks that frequently occur in 
geometry-heavy applications. All these classes should be considered
as being <b>preliminary</b>, meaning that they might be moved to
different packages, or minor details of the interfaces may change,
but their basic functionality will then still be available in a
similar (and possibly extended) form.


# Change log:


Version 0.0.2-SNAPSHOT:

* Added option to create closed `CatmullRomSpline` instances 
  in response to [Issue 1](https://github.com/javagl/Geom/issues/1) 
* Added methods to compute the area of shapes in the `Shapes` class
     
Version 0.0.1, 2015-06-22:

* Initial commit
