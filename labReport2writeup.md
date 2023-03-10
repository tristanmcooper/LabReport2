Tristan Cooper - A17085814
# Lab Report 2: Servers and Bugs

*Hello, and welcome to the Markdown page for my second CSE 15L Lab Report!*

## Part 1: StringServer

In this part, I created a file called `StringServer.java` that works as a web server to allow users to add Strings in the URL.

Here is my code:

```java

import java.io.IOException;
import java.net.URI;

import java.util.ArrayList;

// URLHandler in "Server.java"
class Handler implements URLHandler {

    // Stores "single string"
    // Chose to use an ArrayList to store the strings being added
    ArrayList<String> singleString = new ArrayList<String>();

    // Allows me to return a string in the next method
    public String printArray() {
        String finalString = "";
        for (int i = 0; i < singleString.size(); i++) {
            // Adds line break
            finalString += singleString.get(i)+"\n";
        }
        return finalString;
    }

    public String handleRequest(URI url) {


        // No query, simply display the ArrayList (previous inputs)
        if (url.getPath().equals("/")) {
            String emptyRequest = printArray();
            return emptyRequest;
        }

        else {
            System.out.println("Path: " + url.getPath());

            // Correct request
            if (url.getPath().contains("/add-message")) {

                String[] parameters = url.getQuery().split("=");

                if (parameters[0].equals("s")) {

                    for (int i = 1; i < parameters.length; i++) {
                        // Everything after the "="
                        singleString.add(parameters[i]);
                    }

                    String printedArray = printArray();
                    return printedArray;
                }
            }
            // Invalid request
            return "404 Not Found";
        }
    }
}

class StringServer extends Handler {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between "
            + "1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);
        Server.start(port, new StringServer());

    }
}
```
> As you'll notice, the structure of this code borrows liberally from the starter code given in the [CSE15L `wavelet` repository](https://github.com/ucsd-cse15l-f22/wavelet)

> In fact, the other Java File in my [`LabReport2`](https://github.com/tristanmcooper/LabReport2) GitHub Repository is `Server.java`, copied entirely from the Week 2 Lab. So, thank you to the CSE 15L Staff for supplying the file with the `URIHandler` interface!  


### Adding the first string:

![First](/firstSC.png)

<!--![First Screenshot](https://github.com/tristanmcooper/LabReport2/blob/f3981b691ed128d9b724a908dc7a468bd505cab0/LabReport2%20First%20StringServer%20SCREENSHOT.png)-->
This first screenshot shows me entering the following request into the URL: 

`add-message?s=This is my first String!` 

- __Walkthrough of example one__

1. After submitting this request, the first method to be called is `handleRequest(URI url)`
2. It takes in the url: `localhost:400/add-message?s=This is my first String!` as an argument.
3. `handleRequest` finds that the path *isn't blank*, so it goes on to search for a correct path; one that contains `"/add-message"`
4. Once that is found, it calls `getQuery`, then __splits the query at the equals sign__, then goes on to add every string after the equals sign to an ArrayList called `singleString`
5. Finally, it calls the simple method, `printArray` to return a formatted string that is displayed on the server page!
> So far, the only data fields to be changed are `singleString` and `parameters`. Note that `parameters` is a variable that will be altered with every new request/refresh! This is unlike `singleString`, which will continue to store *all* added strings.


### Let's try adding another string!

![Second](secondSC.png)
<!-- ![Second Screenshot](https://github.com/tristanmcooper/LabReport2/blob/f3981b691ed128d9b724a908dc7a468bd505cab0/2nd%20Screenshot%20showing%20URL%20entry%20(spaces).png) -->

In this screenshot, I've included the actual input that I entered into the URL, so that the `%20` characters representing spaces in the next image make sense.

<div align="center"> Now I press enter: </div> 

![Third](thirdSC.png)
<!-- ![Third Screenshot](https://github.com/tristanmcooper/LabReport2/blob/f3981b691ed128d9b724a908dc7a468bd505cab0/3rd%20SC%20result%20after%20second%20message.png) -->

- __Walkthrough of example one__

1. Just like the last example, the method `public String handleRequest(URI url)` inside the `Handler` class takes in the URL I entered as an argument.
2. When it discovers that there is a valid path, the `parameters` array is re-initialized to store the new query.
3. Next, the for loop iterates, adding each string to my ArrayList, `singleString`
4. Finally, the `printedArray` data field is declared again (or "overwritten" if it helps to think of it that way) by calling our handy method `printArray`.
5. By returning `printedArray`, we are displaying the new, updated list of strings on the web page.



### __To Recap:__ In *both* examples:
- The value of `URI url`, `String s`, `parameters`,`singleString`, and `printedArray`are changed.

## Part 2: Lab 3 Bugs
### __Bug in `ArrayExamples.java`__ `reversed()`

- __*Failure-inducing* input:__
```java
  @Test
  public void testReversedFail() {
    int[] input2 = {1,2,3,4,5};
    assertArrayEquals(new int[] {5,4,3,2,1}, ArrayExamples.reversed(input2));
  }
```
- __Input that *doesn't* induce failure:__

```java
  @Test
  public void testReversedNonFail() {
    int[] input1 = {0,0,0};
    assertArrayEquals(new int[]{0,0,0}, ArrayExamples.reversed(input1));
  }
```

- __Symptoms:__
### <div align="center"> Failing Test Symptom: </div>

![failing screenshot](firstfailsc.png)
![failing terminal sc](termfail1.png) 
![fail term](termfailnew.png)

### <div align="center"> Passing Test Symptom: </div>

![passing sc](passTerm.png)


> Note: I imported `java.util.Arrays` to print symptoms in the terminal, as a passing JUnit test shows no visual to go off of.

- __CODE CORRECTION__
```java
//Return a *new* array with all the elements of the input array in reversed order
```

```java
// BEFORE: Buggy Code
  static int[] reversed(int[] arr) {
    int[] newArray = new int[arr.length];
    for(int i = 0; i < arr.length; i += 1) {
      arr[i] = newArray[arr.length - i - 1];
    }
    return arr;
  }
```

```java
// AFTER: Fixed Code
  static int[] reversed(int[] arr) {
    int[] newArray = new int[arr.length];
    for(int i = 0; i < arr.length; i += 1) {
      newArray[i] = arr[arr.length - i - 1];
    }
    return newArray;
  }
```
### __Why does this fix address the issue?__

In the original method, both assignment and return were flawed. The method created a new array, `newArray`, and assigned the *empty* values in that array to the argument array, `arr`, effectively wiping out all of the original data during iteration.
After that, the method returns the original array instead of the new one.

__So what's the fix?__
The corrected code also creates a new array, `newArray`. But this time, it assigns the values in the argument array, `arr` to `newArray` in reverse order. After the for loop, the method properly returns the `newArray`.


## Part 3: What Did I Learn?

I think learning how to __create and host a web server__, either locally or on UCSD's server was the most exciting and possibly more useful skill I learned in the last two weeks.
Being able to apply my previous Java knowledge to something more practical like URLs opens up a ton of opportunities that I otherwise wouldn't have had before, and potentially a step towards doing Web Development.

# That's it! Thank you for reading my Lab Report!





