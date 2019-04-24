This is a small web based application for SV players using Java servlets.
It tracks a user's collection of cards and displays which pack is best to open in order to maximize the collection.
It uses the SV-portal API to retrieve a list of all available cards.
The card collection still has to be manually entered into the web app.
The web app can updated each expansion by adding its ID number from the API into the Expansion.txt file in the resources folder.


It is inspired by u/El_Basto's tracker from reddit (which was inspired by a Hearthstone version).
However, the algorithm for which pack is best is different as shown below in a simplified example.



Installation (Work in progress for easier deployment): You can install it by setting up a tomcat server from Apache and a Java Runtime environment from Oracle.
Dependencies: Google's gson for parsing.
Compile the source code (including the resources folder) as .war file and place into the webapps folder of the tomcat directory.
Then enter http://localhost:8080/SV_Collection_Tracker/Welcome into your web browser.



Algorithm example for calculating which pack to open based on one's collection:

Let's say we are looking at one expansion. This expansion only have 5 unique Silver cards. It does not have any Bronze, Gold, or Legend for simplicity.
We are going to calculate the expected vial of the pack for this expansion by first calculating the expected vial of each of these cards.

Assume you own:

* Silver A: 1 out of 3 copies
* Silver B: 2 out of 3 copies
* Silver C: 0 out of 3 copies
* Silver D: 3 out of 3 copies
* Silver E: 5 out of 3 copies

The probability of drawing a Silver is 0.25 for slot 1-7 and .925 for slot 8. So the average is 0.334375. (For Gold the average would be .06, Legend would be .015, Bronze would be 0.675*7/8 = 0.590625).

Let's calculate Silver A's expected vial.

There are 5 Silvers, so the average of drawing Silver A is (1/5)*0.334375 or 0.334375/5

Now we have to calculate all the scenarios of drawing from 0 to 8 of Silver A which we own 1 copy of.

For drawing 0: If we draw 0 Silver A, the expected vial for Silver A is 0.
For drawing 1: 8C1 * ((0.334375/5)^1) * (1-(0.334375/5))^(8-1) * (1*200)

Note: 8C1 is 8 choose 1 since order does not matter. The (1*200) is the expected vial of drawing one Silver.

For drawing 2: 8C2 * ((0.334375/5)^2) * (1-(0.334375/5))^(8-2) * (2*200)
For drawing 3: 8C3 * ((0.334375/5)^3) * (1-(0.334375/5))^(8-3) * (2*200 + 1*120)

Note: Since we already own 1, the formula now accounts for extras being liquefied in the form of 1*120.

For drawing 4: 8C4 * ((0.334375/5)^4) * (1-(0.334375/5))^(8-4) * (2*200 + 2*120)
For drawing 5: 8C5 * ((0.334375/5)^5) * (1-(0.334375/5))^(8-5) * (2*200 + 3*120)
For drawing 6: 8C6 * ((0.334375/5)^6) * (1-(0.334375/5))^(8-6) * (2*200 + 4*120)
For drawing 7: 8C7 * ((0.334375/5)^7) * (1-(0.334375/5))^(8-7) * (2*200 + 5*120)
For drawing 8: 8C8 * ((0.334375/5)^8) * (1-(0.334375/5))^(8-8) * (2*200 + 6*120)

Then we add up all of the above to get the expected vial for Silver A. Now we do Silver B, which we own 2 copies of.

For drawing 0: If we draw 0 Silver B, the expected vial for Silver B is 0.
For drawing 1: 8C1 * ((0.334375/5)^1) * (1-(0.334375/5))^(8-1) * (1*200)
For drawing 2: 8C2 * ((0.334375/5)^2) * (1-(0.334375/5))^(8-2) * (1*200 + 1*120)
For drawing 3: 8C3 * ((0.334375/5)^3) * (1-(0.334375/5))^(8-3) * (1*200 + 2*120)
For drawing 4: 8C4 * ((0.334375/5)^4) * (1-(0.334375/5))^(8-4) * (1*200 + 3*120)
For drawing 5: 8C5 * ((0.334375/5)^5) * (1-(0.334375/5))^(8-5) * (1*200 + 4*120)
For drawing 6: 8C6 * ((0.334375/5)^6) * (1-(0.334375/5))^(8-6) * (1*200 + 5*120)
For drawing 7: 8C7 * ((0.334375/5)^7) * (1-(0.334375/5))^(8-7) * (1*200 + 6*120)
For drawing 8: 8C8 * ((0.334375/5)^8) * (1-(0.334375/5))^(8-8) * (1*200 + 7*120)

Then add it all up to get the expected vial for Silver B. Now we do Silver C, which we own 0 copies of.

For drawing 0: If we draw 0 Silver C, the expected vial for Silver C is 0.
For drawing 1: 8C1 * ((0.334375/5)^1) * (1-(0.334375/5))^(8-1) * (1*200)
For drawing 2: 8C2 * ((0.334375/5)^2) * (1-(0.334375/5))^(8-2) * (2*200)
For drawing 3: 8C3 * ((0.334375/5)^3) * (1-(0.334375/5))^(8-3) * (3*200)
For drawing 4: 8C4 * ((0.334375/5)^4) * (1-(0.334375/5))^(8-4) * (3*200 + 1*120)
For drawing 5: 8C5 * ((0.334375/5)^5) * (1-(0.334375/5))^(8-5) * (3*200 + 2*120)
For drawing 6: 8C6 * ((0.334375/5)^6) * (1-(0.334375/5))^(8-6) * (3*200 + 3*120)
For drawing 7: 8C7 * ((0.334375/5)^7) * (1-(0.334375/5))^(8-7) * (3*200 + 4*120)
For drawing 8: 8C8 * ((0.334375/5)^8) * (1-(0.334375/5))^(8-8) * (3*200 + 5*120)

Then add it all up to get the expected vial for Silver C. Now we do Silver D, which we own 3 copies of.

For drawing 0: If we draw 0 Silver D, the expected vial for Silver D is 0.
For drawing 1: 8C1 * ((0.334375/5)^1) * (1-(0.334375/5))^(8-1) * (1*120)
For drawing 2: 8C2 * ((0.334375/5)^2) * (1-(0.334375/5))^(8-2) * (2*120)
For drawing 3: 8C3 * ((0.334375/5)^3) * (1-(0.334375/5))^(8-3) * (3*120)
For drawing 4: 8C4 * ((0.334375/5)^4) * (1-(0.334375/5))^(8-4) * (4*120)
For drawing 5: 8C5 * ((0.334375/5)^5) * (1-(0.334375/5))^(8-5) * (5*120)
For drawing 6: 8C6 * ((0.334375/5)^6) * (1-(0.334375/5))^(8-6) * (6*120)
For drawing 7: 8C7 * ((0.334375/5)^7) * (1-(0.334375/5))^(8-7) * (7*120)
For drawing 8: 8C8 * ((0.334375/5)^8) * (1-(0.334375/5))^(8-8) * (8*120)

Note: notice the (1*120) being used instead of (1*200) for draw 1.

Then add it all up to get the expected vial for Silver D. Now we do Silver E, which we own 5 copies of.

Silver E expected vial should be the same as Silver D since we are getting extras for draw 1-8 as well.


Finally add up Silver A - Silver E expected vial to get the expected vial for the expansion.


Please feel free to let me know if you feel something is off with this algorithm at henryvu.contact@gmail.com.