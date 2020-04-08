# Bond_Reward_to_Risk
Bond Calculations

Bond_Reward_to_Risk

I created a new project with Java to analyze concepts in fixed income securities. I created this in order to display my diversity of programming skills with my first Java project, and I also wanted to do something with fixed income analysis. The program will work through a database of bonds and make calculations for each one. The script itself creates 4,320 different hypothetical that are used in place of inputting real bonds. I wanted to create a reward to risk ratio using duration, yield to maturity, and convexity. I did this by taking yield to maturity over modified duration discounted by a convexity factor. In equity securities we use the Sharpe formula to find a reward to risk ratio, and I wanted to create something similar for bonds.  

Duration and convexity measure how sensitive a bond is to interest rate changes. Whenever market interest rates change, existing bonds have their prices change. So, a bond holder can see the value of their bonds go up or down depending on the market movements. Looking at bonds as liquidity and collateral, changes in interest rates can pose a risk that can change the value of the portfolio for the bondholder. Some bonds may be more sensitive to others. So, one bond may change drastically while another bond may not change very much at all. We can examine the sensitivity to interest rate changes with modified duration and the bond’s convexity. The modified duration can predict the price change with interest rate changes, but we also need convexity because the problem with duration is that it predicts a linear change when actually the duration itself can often change when the bond’s price changes. When price and interest rate changes are plotted on a graph, we can find that the relationship is not always a straight line.  We call this curvature convexity, and we can discover that more convexity is good for investors because it will lower the impact of falling prices but emphasize the impact of rising prices.  

In order to run calculations of the bonds I first wanted to create a hypothetical database of bonds. I created a table with each possible combination of different maturities, coupons, and principles. The combinations were of $1,000, $5,000, and $10,000 principles, coupons ranging from 0 to 15 percent, and periods to maturity up to 30 years, annual and semi-annual. For all possible combinations I needed to programmatically create Java methods to populate the table with each possible bond. I decided the best way to populate the database would be by establishing a decision tree structure that would branch out to all combinations, then I found the patterns that would be in the tree and the patterns each bond property would have when they are deposited into column in a table.  

For each of the 4,320 bonds I calculated duration, modified duration, and my own kind of calculation for convexity. I decided to make my own convexity calculation because I thought my approach would be simpler than some others I have seen. What I did was calculate modified duration and then calculate it again for plus one percent in the discount rate. Then I would calculate the rate of change between the two durations. A bond with a high rate of change will have a higher convexity than a bond with little to no change.  

When I made the reward to risk ratio, I used yield to maturity as the numerator over the interest rate sensitivity in the denominator. It may be true that in some cases an investor would not hold a bond till maturity and would not be interested in yield to maturity, and so instead could use a shorter-term current yield calculation as the numerator. In the denominator I put a modified-modified duration of the bond. A high duration means risk is relatively high, although a high convexity can lower the risk associated with the bond, so I make a discount to the durations by multiplying them by 1 – convexity to shrink them by the convexity factor. Then, with this reward to risk ratio, the higher the numerator the higher the reward, and the smaller the sensitivity in the denominator the smaller the risk.  

After creating the database and calculations for all the bonds, I sent the data to a CSV file and then opened the file in Excel and used some basic analysis with tables and pivot tables. My calculations confirm the established facts that shorter time to maturity and higher coupons lower the sensitivity to interest rate changes. Not surprisingly then, the top three risk to reward ratios where held by six-month and one-year to maturity bonds.  

Creating Bond Database 

====================== 

The Bond Database will be populated by combinations of various bond properties. For instance, if coupon rates are the property and if 3 is the range then we have three bonds, a 1%, 2%, and 3% bond. If we add principles of 1,000 and 5,000 we have six total combinations of bonds. That is the n count of each property multiplied by the next. Therefore, three possible coupons and two possible principles = 2 * 3 = 6. As there is a 1,000 principle with, 1%, 2%, and 3% and so on.  
  
To programmatically create the database of bonds, the bonds themselves must be produced in a table and given their proper properties in each column. Therefore, the bond needs property x, y, z and so on, to be inserted into it programmatically. Each bond is a row in the table. Each column of the table will be a vertical stack of properties in the proper order related to other columns as rows, to cycle through all the combinations of that specific property in each of the bonds.   

The table will consist of equal length lists, where each list is treated as a vertical column. Stacked vertically together, the lists have indexes that will make rows specifying each bond.   

In order to achieve this table, the correct cycles of the properties in the columns must be solved in connection with the other columns. In the below tree there are four possible combinations of the properties: [a, 1, +], [a, 1, -], [a, 2, +], [a, 2, -]. If this were organized into a table: 

a 1 + 
a 1 - 
a 2 + 
a 2 - 

Here the 'a' duplicates and cycles every time because it is the first and only. The 1 and 2 duplicate once and do not cycle, or, actually only cycle once in that they appear only once. The + and - do not duplicate but do cycle once, and therefore appear twice.  

Duplicate is to appear more than one time in a row like how above a is followed by a, and to cycle is to have its the property appear again after other properties like above how the + was separated by a - before another cycle of + appeared.  

The duplications and cycles can be calculated so that the lists that make the columns can be created with the correct cycles. It will be important that the order of the tree be kept consistent when calculating. Each "column" in a decision tree holds a specific property category. Below, the first column is "a", second is 1 or 2, and the third is + or -. If n stands for the number of choices, the choice in column one has an n of 1, column two an n of 2, and column three an n of 2. So + and - is two possible outcomes so n = 2.  

The duplication number can be found by multiplying 1 * all forward n values together. And the cycle number can be found by multiplying 1 * all backwards n values together. Except for the first and last columns in the tree, the cycle calculation for the first column will be 1, and the duplicate calculation for the last column be 1.    

So we know that in the list of possible values of "a" in the first column of the table, we will have 1 * 2 * 2 = 4 duplicates and because it is the first element in the chain it will be 1. The second column  1 * 2 = two duplicates, and, 1 * 1 = one cycle. And lastly for the last column, the duplicate is simply 1 and cycle is 2 * 1 = 2 cycles. All of these calculations can be verified in the table above.  

So by creating lists of the proper duplications and cycles, this process can create columns that can be aligned to create a table of bonds with combination properties.  

  

         [+] 

        /         

     [1]  

    /   \   

   /       [-]  

[a]        

   \       [+] 

    \   / 

     [2]     

        \    

         [-] 

           

 
