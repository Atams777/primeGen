package com.theirongrizzly.primegen;
/*
 * Here's what we're going to do, first we're going to have 4 arrays, each position in the array is an digit from the int
 *
 * so 12456789 == [9,8,7,6,5,4,3,2,1], this is inverted just to make upping the next digit easier
 * Now that we have the arrays started, we're going to ship them off to threads
 *
 * That way we can do all 4 of the base digits at the same time
 *
 * After that
 */

/*
 * change log
 * Version 0.1 will use 4 threads just to make life easier
 */
public class main {
    public static void main(String[] args){
        /*
         * All primes have to end in 1, 3, 7, or 9
         * 0 means it can be divided by 2
         * 2,4,6,8 all 2
         * 5, is 5
         */
        // Hey look multithreaded
        reportThread reportThread = new reportThread();
        reportThread.setPollTime(10);
        reportThread.start();

        intThread threadOne = new intThread(1, reportThread);
        intThread threadThree = new intThread(3, reportThread);
        intThread threadSeven = new intThread(7, reportThread);
        intThread threadNine = new intThread(9, reportThread);

        threadOne.start();
        threadThree.start();
        threadSeven.start();
        threadNine.start();

    }
}
