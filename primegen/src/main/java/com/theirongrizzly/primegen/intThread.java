package com.theirongrizzly.primegen;
/*
    Header info...
    Author: Tyler J. Mitchell
 */
import java.util.ArrayList;

/**
 *
 */
public class intThread extends Thread{
    ArrayList<Integer> intArray = new ArrayList<>();
    ArrayList<Integer> digitToCheck = new ArrayList<>();
    boolean run = true;
    reportThread reportThread;
    public intThread(int tempStartingValue, reportThread tempReportThread) {
        intArray.add(tempStartingValue);
        reportThread = tempReportThread;
    }

    /**
     * Run our thread that looks for prime numbers
     */
    @Override
    public void run(){
        int i = 0;
        while(run){
            moveToNext(1);
            if(isPrime()){
                reportThread.addPrime(intArray);
            }
            i++;
            if(i >= 1000){
                run = false;
            }
        }
        utils.printDebug("MyThread running");
    }

    /**
     * This moves us to the next potential prime number, this is a recursive function, so if we're told to move up a digit
     *   and that digit happens to be a 9, move the next digit up one also
     * @param i int the digit in the array that we're going to move up by one
     *
     */
    private void moveToNext(int i){
        if(i == intArray.size()){
            intArray.add(1);
        }else {
            int val = intArray.get(i);
            if (val >= 9) {
                intArray.set(i++, 0);
                moveToNext(i);
                return;
            }else{
                intArray.set(i, val+1);
            }
        }
        utils.printDebug("intArray going out: " + intArray);
    }

    /**
     * We're going to start our check to see if this is a prime number
     * @return boolean True for a Prime, False for not a prime
     */
    private boolean isPrime(){
        digitToCheck.clear();
        digitToCheck.add(1);
        int[] digits = new int[]{ 2,4,2,2};
        int i = 0;
        // if top digit of digitToCheck > 1/2 the top digit of intArray we're done
        // This is inefficient, but I'll look to optimize later
        while(!digitToCheckMoreThanHalfPotentialPrime()){
            increaseDigitToCheck(digits[i++]);
            if(digitToCheckMoreThanHalfPotentialPrime()){
                break;
            }else if(evenlyDivided()){
                return false;
            }else if(i >= 3){
                i = 0;
            }
        }

        return true;
    }
    /**
     * So division is just repeated subtraction...
     * This is where we spend most of our time, there is a way we could speed this up
     * 1) Only use prime numbers to check if it's prime. Any non-prime number is divisible by a prime so we don't need to check that.
     *      i.e. 21 is one of the numbers we would use to see if this is a prime, but 21 isn't prime, therefore if we just
     *      check with 3 we don't need to check with 21
     *      To do this we would need to coordinate between the threads and have an array of primes to check
     *      This could lead to a race condition where a prime is skipped
     *      This is probably the most efficient improvement, but comes with the most work
     *      Does eventually become a problem when you're trying to store a bunch of massive primes in memory
     */
    private boolean evenlyDivided(){
        int i;
        ArrayList<Integer> tempDigitToCheck = new ArrayList<>();
        ArrayList<Integer> potentialPrime = new ArrayList<>(intArray);
        int places;
        int subPos;
        int digitsAdded = 0;
        while(true){
            subPos = 0;
            if(potentialPrime.size() < digitToCheck.size()){
                return false;
            }else if(potentialPrime.size() - digitToCheck.size() > 1){
                tempDigitToCheck.clear();
                digitsAdded = potentialPrime.size() - digitToCheck.size() - 1;
                i = 0;
                while(i < digitsAdded){
                    tempDigitToCheck.add(0);
                    i++;
                }
                tempDigitToCheck.addAll(digitToCheck);
                digitToCheck.clear();
                digitToCheck.addAll(tempDigitToCheck);
            }
            places = digitToCheck.size()-1;
            while(subPos <= places) {
                potentialPrime.set(subPos, potentialPrime.get(subPos) - digitToCheck.get(subPos));
                subPos++;
            }
            subPos = 0;
            i = potentialPrime.size()-1;
            while(subPos <= i) {
                if (subPos == i && potentialPrime.get(subPos) < 0){
                    return false;
                }else if (subPos == i && potentialPrime.get(subPos) == 0){
                    while(i >= 0){
                        if(potentialPrime.get(i) == 0){
                            potentialPrime.remove(i);
                        }
                        i--;
                    }
                }else if (potentialPrime.get(subPos) < 0){
                    potentialPrime.set(subPos, potentialPrime.get(subPos)+10);
                    potentialPrime.set(subPos+1, potentialPrime.get(subPos+1)-1);
                }
                if(potentialPrime.size() == 0){
                    return true;
                }
                subPos++;
            }
            while(digitsAdded > 0){
                digitToCheck.remove(0);
                digitsAdded--;
            }
        }
    }

    private void increaseDigitToCheck(Integer num){
        int i = digitToCheck.get(0)+num;
        boolean increasingNextDigit = i > 9;
        int pos = 0;
        if(!increasingNextDigit){
            digitToCheck.set(pos, i);
        }
        while(increasingNextDigit) {
            digitToCheck.set(pos, i - 10);
            pos++;
            if(pos == digitToCheck.size()){
                digitToCheck.add(1);
                increasingNextDigit = false;
            }else if (digitToCheck.get(pos) + 1 < 10) {
                digitToCheck.set(pos, digitToCheck.get(pos) + 1);
                increasingNextDigit = false;
            }
        }
    }

    private boolean digitToCheckMoreThanHalfPotentialPrime(){
        int i = intArray.size();
        ArrayList<Integer> potentialPrime = new ArrayList<>(intArray);
        if(digitToCheck.size() <= i-2){
            return false;
        }else if(digitToCheck.size() > potentialPrime.size()){
            return true;
        }else if(digitToCheck.size() == potentialPrime.size() && potentialPrime.get(i-1)/2 < digitToCheck.get(i-1)){
                return true;
        }
        // intellij optimization, if the top 2 digits, divided by 2, are greater than the top digit of digit to check return true
        // otherwise false
        return ((potentialPrime.get(i-1) * 10)+potentialPrime.get(i-2)) / 2 <= (digitToCheck.get(digitToCheck.size() - 1));

    }
}