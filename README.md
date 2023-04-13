# COP4520-Assignment-3

## Problem 1: The Birthday Presents Party

### Problem statement:

The Minotaur’s birthday party was a success. The Minotaur received a lot of presents
from his guests. The next day he decided to sort all of his presents and start writing
“Thank you” cards. Every present had a tag with a unique number that was associated
with the guest who gave it. Initially all of the presents were thrown into a large bag with
no particular order. The Minotaur wanted to take the presents from this unordered bag
and create a chain of presents hooked to each other with special links (similar to storing
elements in a linked-list). In this chain (linked-list) all of the presents had to be ordered
according to their tag numbers in increasing order. The Minotaur asked 4 of his servants
to help him with creating the chain of presents and writing the cards to his guests. Each
servant would do one of three actions in no particular order: 1. Take a present from the
unordered bag and add it to the chain in the correct location by hooking it to the
predecessor’s link. The servant also had to make sure that the newly added present is
also linked with the next present in the chain. 2. Write a “Thank you” card to a guest and
remove the present from the chain. To do so, a servant had to unlink the gift from its
predecessor and make sure to connect the predecessor’s link with the next gift in the
chain. 3. Per the Minotaur’s request, check whether a gift with a particular tag was
present in the chain or not; without adding or removing a new gift, a servant would scan
through the chain and check whether a gift with a particular tag is already added to the
ordered chain of gifts or not. As the Minotaur was impatient to get this task done
quickly, he instructed his servants not to wait until all of the presents from the
unordered bag are placed in the chain of linked and ordered presents. Instead, every
servant was asked to alternate adding gifts to the ordered chain and writing “Thank you”
cards. The servants were asked not to stop or even take a break until the task of writing
cards to all of the Minotaur’s guests was complete. After spending an entire day on this
task the bag of unordered presents and the chain of ordered presents were both finally
empty! Unfortunately, the servants realized at the end of the day that they had more
presents than “Thank you” notes. What could have gone wrong? Can we help the
Minotaur and his servants improve their strategy for writing “Thank you” notes? Design
and implement a concurrent linked-list that can help the Minotaur’s 4 servants with this
task. In your test, simulate this concurrent “Thank you” card writing scenario by
dedicating 1 thread per servant and assuming that the Minotaur received 500,000
presents from his guests.

### How to run:

Navigate to the directory where the program files are located, then run the following commands in the command line:

```
javac p1.java
java p1
```

### Explanation:

This approach uses the Lock free list implemented in 9.8 of the Art of Multiprocessor Programming textbook. Each of the 4 servants is represented by a thread, and the chain of presents to tag and write thank-you cards for is represented by a lock-free list. The unordered bag of gifts is represented by an arraylist which is shuffled. We store the amount of presents added and removed from the chain in atomic integers, and while there are more presents that need to be tracked, each servant takes presents from the unordered bag and adds them to the chain. When a present is removed from the chain, a thank you card is written with it.

### Experimental Evaluation:

The program takes 2 ms for 50 presents, 10 ms for 500 presents, 57 ms for 5000 presents, 4392 ms for 5000 presents, and 568914 ms to run for the 500000 presents specified, and data racing is used as each thread is constantly taking presents from the unordered bag and adding them to the chain. The program runs until all presents have had thank you cards written for them. The lock-free list approach guarantees that progress is always made.

The servants could have had more presents than thank you notes if they skipped over some presents while they removed presents from the chain, because thank you notes are written when presents are removed. To fix this problem, the problem strictly runs until the specified number of presents have been taken from the chain, using atomic integers.

## Problem 2: Atmospheric Temperature Reading Module

### Problem statement:

You are tasked with the design of the module responsible for measuring the atmospheric
temperature of the next generation Mars Rover, equipped with a multicore CPU and 8
temperature sensors. The sensors are responsible for collecting temperature readings at
regular intervals and storing them in shared memory space. The atmospheric
temperature module has to compile a report at the end of every hour, comprising the top
5 highest temperatures recorded for that hour, the top 5 lowest temperatures recorded
for that hour, and the 10-minute interval of time when the largest temperature
difference was observed. The data storage and retrieval of the shared memory region
must be carefully handled, as we do not want to delay a sensor and miss the interval of
time when it is supposed to conduct temperature reading. Design and implement a
solution using 8 threads that will offer a solution for this task. Assume that the
temperature readings are taken every 1 minute. In your solution, simulate the operation
of the temperature reading sensor by generating a random number from -100F to 70F at
every reading. In your report, discuss the efficiency, correctness, and progress guarantee
of your program.

### How to run:

Navigate to the directory where the programs are located, then run the following commands in the command line:

```
javac p2.java
java.p2
```

### Explanation:

The temperatures recorded by each of the 8 sensors is stored in an arraylist of arraylists, with there being a list of integers containing the temperatures recorded by each sensor. The rover class, which extends thread, collects a temperature in the range -100F to 70F each minute and stores each value in the list, and this runs concurrently for each of the 8 sensors. After each hour, a report is created, containing the top 5 highest and lowest temperatures from that hour, and the 10-minute interval with the greatest temperature difference. Intervals are made by dividing the temperatures recorded into separate lists.

### Experimental Evaluation:

For 5 hours, this program takes 68843 ms. The threads run concurrently with each individual list of temperatures being added to the greater arraylist. Temperatures are continually being taken each hour. Arraylists have an o(1) runtime for add operations and the Arrays.sort method used in generating the report has an o(nlogn) runtime which is why arraylists were chosen.

