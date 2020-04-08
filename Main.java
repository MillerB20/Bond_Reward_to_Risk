package Main;

import org.apache.poi.ss.formula.functions.Irr;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {

    // Make list of IDs for each bond
    private static String[] bond_ID_list(int x) {

        String[] bond_ID_list = new String[x];

        for (int i = 0; i < x; i++) {
            bond_ID_list[i] = Integer.toString(i + 1);
        }

        return bond_ID_list;
    }

    // Make list of principles for each bond
    private static int[] bond_principle_list(int x, int y) {

        // Order 1 (semi annual):
        // Bond principle is first column in tree.
        // Duplication = 1 * 16 * 60 = 960
        // Cycle = first column in tree = 1
        int duplication = 960, active_property = 0, a = 0;
        int[] bond_principle_list = new int[x + y];

        //int p = 0 ;

        for (int i = 0; i < x; i++) {

            // Duplication
            if (i % duplication == 0) {
                // Cycle
                if (a > 1) {
                    active_property = 1_0000;
                    a = 0;
                } else if (a == 1) {
                    active_property = 5_000;
                    a = a + 1;
                } else if (a == 0) {
                    active_property = 1_000;
                    a = a + 1;
                }
            }

            bond_principle_list[i] = active_property;
        }

        // Order 2 (annual):
        // Bond principle is first column in tree.
        // Duplication = 1 * 16 * 30 = 480
        // Cycle = first column in tree = 1

        duplication = 480;

        for (int i = x; i < x + y; i++) {

            // Duplication
            if (i % duplication == 0) {
                // Cycle
                if (a > 1) {
                    active_property = 1_0000;
                    a = 0;
                } else if (a == 1) {
                    active_property = 5_000;
                    a = a + 1;
                } else if (a == 0) {
                    active_property = 1_000;
                    a = a + 1;
                }
            }

            bond_principle_list[i] = active_property;
        }

        return bond_principle_list;
    }

    // Make list of coupon rates for each bond
    private static int[] coupon_list(int x, int y) {

        // Coupon rate is second column in tree.
        // Duplication = 1 * 60 = 60
        // Cycle = 1 * 3 = 3
        int duplication = 60, active_property = 0, a = 0;
        int[] coupon_list = new int[x + y];
        int[] property_list = {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};

        for (int i = 0; i < x; i++) {

            // Duplication
            if (i % duplication == 0) {
                // Cycle
                if (a > 14) {
                    active_property = property_list[a];
                    a = 0;
                } else {
                    active_property = property_list[a];
                    a = a + 1;
                }
            }
            coupon_list[i] = active_property;
        }

        // Coupon rate is second column in tree.
        // Duplication = 1 * 30 = 30
        // Cycle = 1 * 3 = 3
        duplication = 30;

        for (int i = x; i < x + y; i++) {
            // Duplication
            if (i % duplication == 0) {
                // Cycle
                if (a > 14) {
                    active_property = property_list[a];
                    a = 0;
                } else {
                    active_property = property_list[a];
                    a = a + 1;
                }
            }
            coupon_list[i] = active_property;
        }

        //System.out.println(Arrays.toString(coupon_list));
        return coupon_list;

    }

    // Make list of periods to maturity for each bond
    private static float[] maturity_list(int x, int y) {

        // Coupon rate is last column in tree.
        // Duplication = last column = 1
        // Cycle = 1 * 3 * 15 = 45
        // Total times any given element appears = 45
        // 2,700 / 45 = 60 elements to loop through
        float a = 30.00f;
        float[] maturity_list = new float[x + y];

        for (int i = 0; i < x; i++) {
            // Cycle
            if (a == 0) {
                a = 30.00f;
            }
            maturity_list[i] = a;
            a = a - 0.50f;
        }

        // Coupon rate is last column in tree.
        // Duplication = last column = 1
        // Cycle = 1 * 3 * 15 = 45
        // Total times any given element appears = 45
        // The re-count to start a new cycle is (total: 3 * 15 * 30 = 1,350)
        a = 30.00f;

        for (int i = x; i < x + y; i++) {
            // Cycle
            if (a == 0) {
                a = 30.00f;
            }
            maturity_list[i] = a;
            a = a - 1.00f;
        }

        return maturity_list;
    }

    // Make list of "period types" for semi-annual or annual
    private static String[] period_type_list(int x, int y) {

        String[] period_type_list = new String[x + y];

        for (int i = 0; i < x + y; i++) {
            if (i < x) {
                period_type_list[i] = "s";
            } else {
                period_type_list[i] = "a";
            }
        }

        return period_type_list;
    }

    // Make list of present values for each bond
    private static float[] bond_prices(int[] y, int[] z, float[] a,
                                       int b, int c) {

        float principle, coupon, bond_price_float, discount_rate, maturity;
        double discount, cpn_pmnt, dis_cash_flow, bond_price;
        String period_type;
        float[] bond_prices = new float[b + c];

        for (int i = 0; i < b + c; i++) {
            principle = y[i];
            coupon = z[i];
            maturity = a[i];

            if (i < b) {
                period_type = "s";
            } else {
                period_type = "a";
            }

            cpn_pmnt = coupon * .01;
            cpn_pmnt = cpn_pmnt * principle;
            discount_rate = 0.05f;
            bond_price = 0.00;

            if (period_type.equals("s")) {
                cpn_pmnt = cpn_pmnt / 2;
                discount_rate = discount_rate / 2;
                maturity = maturity * 2;
            }

            for (int ii = 0; ii < maturity; ii++) {
                discount = Math.pow(1.00 + discount_rate, ii + 1);

                if (ii == maturity - 1) {
                    dis_cash_flow = (principle + cpn_pmnt) / discount;
                } else {
                    dis_cash_flow = cpn_pmnt / discount;
                }

                bond_price = bond_price + dis_cash_flow;
            }

            // Rounding two decimal places with two zeros in the 100
            bond_price = Math.round(bond_price * 100.0) / 100.0;

            bond_price_float = (float) bond_price;

            bond_prices[i] = bond_price_float;

        }

        return bond_prices;
    }

    // Make list of YTM (IRR) for each bond
    private static double[] irr (int[] p, float[] bp, int[] cp, float[] m, int x, int y){

        // The price of the bond is the PV discounted at 5% so
        // the IRR, or, YTM, is always 5%

        double the_return, coupon;
        double bond_price, maturity;
        int maturity_int, principle, irr_period_multiplyer;
        String period_type;
        double[] irrs = new double[x + y];

        for(int i = 0; i<x+y; i++){

            bond_price = bp[i];
            bond_price = -1 * bond_price;
            maturity = m[i];
            principle = p[i];
            coupon = cp[i] * .01;
            coupon = coupon * principle;

            if (i < x) {
                coupon = coupon / 2;
                maturity = maturity * 2;
                irr_period_multiplyer = 2;
            } else {
                irr_period_multiplyer = 1;
            }

            maturity_int = (int) maturity;
            double[] cash_flows = new double[maturity_int + 1];

            cash_flows[0] = bond_price;
            for (int ii = 1; ii < maturity_int; ii++){
                cash_flows[ii] = coupon;
            }
            cash_flows[maturity_int] = coupon + principle;

            the_return = Irr.irr(cash_flows, .01);
            the_return = Math.round(the_return * 10000.0) / 10000.0;
            the_return = the_return *  irr_period_multiplyer;

            irrs[i] = the_return;
        }
        return irrs;
    }

    // Dual function to make list of modified duration and modified duration for + 1 to the discount rate
    // The second modified duration is used to calculate convexity, where the rate of change from
    // the first to second modified duration is the convexity measure.
    private static double[] duration_duration2 (int[] prin, int[] cp, float[] m,
                                                int x, int y, int c){

        double rate, coupon, maturity, discount_rate, weighted_cash_flow, mdur;
        double price, discount_cash_flow, time_weighted_cfs = 0;
        double[] durations = new double[x + y];
        float weight, weight_growth;
        int principle, maturity_int;

        for (int i = 0; i < x + y; i++){

            if(c == 0){
                rate = .05;
            }else{
                rate =  .05 + .01;
            }

            time_weighted_cfs = 0;
            price = 0;
            principle = prin[i];
            coupon = (cp[i] * .01) * principle;
            maturity = m[i];

            if (i < x) {
                coupon = coupon / 2;
                maturity = maturity * 2;
                rate = rate / 2;
                weight_growth = 0.5f;
                weight = .5f;
            } else {
                weight = 1;
                weight_growth = 1.0f;
            }

            maturity_int = (int) maturity;

            for(int ii = 0; ii < maturity_int; ii++){

                discount_rate = Math.pow((1 + rate), ii + 1);

                if (ii == maturity_int - 1){
                    discount_cash_flow = (coupon + principle)/discount_rate;
                    weighted_cash_flow = ((coupon + principle)/discount_rate) * (weight);
                } else{
                    discount_cash_flow = coupon/discount_rate;
                    weighted_cash_flow = (coupon/discount_rate) * (weight);
                }

                price = price + discount_cash_flow;
                time_weighted_cfs = time_weighted_cfs + weighted_cash_flow;
                weight = weight + weight_growth;

            }

            mdur = time_weighted_cfs / price;
            mdur = mdur/(1 + rate);
            mdur = Math.round(mdur * 100.0 ) / 100.0;

            durations[i] = mdur;

        }

        return durations;
    }

    // Make list of convexities as the rate of change in the durations
    private static double[] convexity (double[] d1, double[] d2, int x, int y){

        double convexity;
        double[] convexities = new double[x + y];

        for(int i = 0; i < x + y; i++){
            convexity = (d2[i] - d1[i]) / d1[i];
            convexity = Math.round(convexity * 1000.0 ) / 1000.0;
            convexity = Math.abs(convexity);
            convexities[i] = convexity;
        }

        return convexities;
    }

    // Make list of modified duration * (1 - convexity)
    private static double[] dur_conv (double[] d1, double[] d2, int x, int y){

        double convexity;
        double dur_conv;
        double[] dur_convs = new double[x + y];

        for(int i = 0; i < x + y; i++){
            convexity = Math.abs((d2[i] - d1[i]) / d1[i]);
            dur_conv =  d1[i] * (1 - convexity);
            dur_conv = Math.round(dur_conv * 1000.0 ) / 1000.0;
            dur_convs[i] = dur_conv;
        }

        return dur_convs;
    }

    // Make list of YTM / [duration * (1 - convexity)]
    private static double[] reward_to_risk (double[] irr, double[] dc, int x, int y){

        double[] reward_to_risk_list = new double[x + y];

        for(int i = 0; i < x + y; i++){
            reward_to_risk_list[i] = Math.round((irr[i] / dc[i]) * 1000.00) / 1000.0;

        }

        return reward_to_risk_list;
    }

    // Take all lists and write them to a CSV file as columns
    private static void output_csv(String[] bond_ids,int[] principles, int[] coupon_list,
                                   float[] maturity_list, String[] period_type_list, float[] bond_prices,
                                   double[] irr_list, double[] durations, double[] convexities,
                                   double[] dur_convs, double[] reward_to_risk, int x, int y, String path){

        String s1 = "";

        try{
            File myObj = new File(path);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(path);
            myWriter.write("ID, Principle, Coupon, Years to Maturity, Period Type, Present Value, " +
                    "YTM, Duration, Convexity, DurConv, Reward to Risk" + "\r\n");
            myWriter.close();
        }
        catch (Exception e){
            System.out.println("Failure");
            System.exit(0);
        }

        for (int i = 0; i < x + y; i++){
            s1 =  s1 + bond_ids[i] + ", " + principles[i] + ", " + coupon_list[i]
                    + ", " + maturity_list[i] + ", " + period_type_list[i] + ", " +
                    bond_prices[i]  + ", " + irr_list[i] + ", " +
                    durations[i] + ", " +  convexities[i] + ", " +
                    dur_convs[i] + ", " + reward_to_risk[i] + ", " + "\r\n";

        }

        s1 = s1.substring(0,s1.length() - 4);

        try{
            FileWriter myWriter = new FileWriter(path, true);
            myWriter.write(s1);
            myWriter.close();
        } catch (Exception e){
            System.out.println("Failure");
            System.exit(0);
        }
    }

    public static void main (String[]args){

        Scanner user_input = new Scanner(System.in);
        String path;

        System.out.println("\nSystem Starting...\n");

        // Order of Properties 1 (semi-annual):
        // Principle: 1,000, 5,000, 10,000
        // Coupon: 15, 14 ... 0
        // Time to maturity: 30, 29.50 ... 00.00
        int order_one_total = 3 * 16 * 60;

        // Order of Properties 1 (annual):
        // Principle: 1,000, 5,000, 10,000
        // Coupon: 15, 14 ... 0
        // Time to maturity: 30, 29 ... 00
        int order_two_total = 3 * 16 * 30;

        int c = 0;

        // Raw Data
        // ===========
        String[] bond_ids = bond_ID_list(order_one_total + order_two_total);
        int[] principles = bond_principle_list(order_one_total, order_two_total);
        int[] coupon_list = coupon_list(order_one_total, order_two_total);
        float[] maturity_list = maturity_list(order_one_total, order_two_total);
        String[] period_type_list = period_type_list(order_one_total, order_two_total);

        // Analysis
        // ===========
        float[] bond_prices = bond_prices(principles, coupon_list, maturity_list,
                order_one_total, order_two_total);
        double[] irr_list = irr(principles, bond_prices, coupon_list, maturity_list, order_one_total, order_two_total);
        double[] durations = duration_duration2(principles, coupon_list, maturity_list,
                order_one_total, order_two_total, c);
        c = c + 1;
        double[] durations2 = duration_duration2(principles, coupon_list, maturity_list,
                order_one_total, order_two_total, c);
        double[] convexities = convexity(durations, durations2, order_one_total, order_two_total);
        double[] dur_convs = dur_conv(durations, durations2, order_one_total, order_two_total);
        double[] reward_to_risk = reward_to_risk(irr_list, dur_convs, order_one_total, order_two_total);

        // Return and write to CSV
        // ============================
        System.out.print("Enter Full Save Location Path and CSV File Name: ");
        path = user_input.nextLine();
        path = path.trim();

        output_csv(bond_ids, principles, coupon_list, maturity_list, period_type_list,
                bond_prices, irr_list, durations,  convexities, dur_convs,reward_to_risk,
                order_one_total, order_two_total, path);

        System.out.println("\nComplete");
    }

}