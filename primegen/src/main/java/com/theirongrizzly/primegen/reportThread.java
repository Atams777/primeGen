package com.theirongrizzly.primegen;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/* TODO: this just dumps out numbers as it gets them, it would be better if it printed them in order of size
 *      Couple of ways to do this: have the array hold the last X digits and then just print the smallest at X+1
 *          Downside is this isn't technically going to give you the smallest prime everytime, you could get one thread
 *          way ahead of the others and it could cause them to be out of order
 *      Next option: Always have the smallest entry from each thread saved, then with each new entry print out the
 *          smallest digit, downside: lost of data management, need to think about this more to see if it will actually work
 *      Next Option: annotate the function to say the primes don't come back in order, easy potentially less usable
 *          That said most prime generation is for generating the largest prime possible, so I'm not sure this is actually
 *          a problem in most cases
 */

public class reportThread extends Thread{
    boolean run = true;
    //1000 is a magic number, but I don't think we're going to find that many primes that fast
    ArrayBlockingQueue<String> reportQueue =  new ArrayBlockingQueue<>(1000);
    boolean pollTimeSet = false;

    public int getPollTime() { return pollTime; }

    public void setPollTime(int pollTime) {
        pollTimeSet = true;
        this.pollTime = pollTime;
    }

    int pollTime;

    private int attemptsSinceLastPrime = 0;
    public void run(){
        if(!pollTimeSet){
            System.out.println("poll time wasn't set, defaulting to 10 seconds");
            pollTime = 10;
        }
        String newPrime;
        while (run){
            try {
                //Doing this as a poll, so that we don't get locked waiting forever for our next prime
                newPrime = reportQueue.poll(pollTime, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                newPrime="";
            }

            if(newPrime != null && newPrime.equals("")){
                attemptsSinceLastPrime++;
                System.out.println("It's been " + attemptsSinceLastPrime*pollTime + " since we last found a prime" );
            }else{
                attemptsSinceLastPrime = 0;
                System.out.println("New prime: "+ newPrime);
            }
        }
    }

    /**
     * Adds a prime number to the print queue
     *
     * @param temp ArrayList<Integer>
     *             The prime we're going to add
     *
     * We have a suppresswarnings here because it doesn't like the type cast
     *
     */
    public void addPrime(ArrayList<Integer> temp){
        reportQueue.add(utils.messyArrayToString(temp));
    }

}
