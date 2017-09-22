package main.cs5340.alafleur;

/**
 * Simple pair class. I can't believe they don't have something like this
 */
public class Triplet<A, B, C> {

    private A obj1;
    private B obj2;
    private C obj3;

    public Triplet(A _obj1, B _obj2, C _obj3) {
        obj1 = _obj1;
        obj2 = _obj2;
        obj3 = _obj3;
    }


    public A getObj1() {
        return obj1;
    }

    public B getObj2() {
        return obj2;
    }

    public C getObj3() {
        return obj3;
    }
}
