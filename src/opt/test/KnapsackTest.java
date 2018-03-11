package opt.test;

import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * A test of the knapsack problem
 *
 * Given a set of items, each with a weight and a value, determine the number of each item to include in a
 * collection so that the total weight is less than or equal to a given limit and the total value is as
 * large as possible.
 * https://en.wikipedia.org/wiki/Knapsack_problem
 *
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class KnapsackTest {
    /** Random number generator */
    private static Random random = new Random(0xABCD);
    /** The number of items */
    private static int NUM_ITEMS = 40;
    /** The number of copies each */
    private static int COPIES_EACH = 4;
    /** The maximum value for a single element */
    private static double MAX_VALUE = 50;
    /** The maximum weight for a single element */
    private static double MAX_WEIGHT = 50;
    /** The maximum weight for the knapsack */
    private static double MAX_KNAPSACK_WEIGHT = 0;

    private static int iters = 1000;
    private static int temp = 100;
    private static double decay = 0.95;
    private static int samples = 200;
    private static int toKeep = 20;
    private static int popSize = 200;
    private static int toMutate = 10;
    private static int toMate = 100;


    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {

        int alg = 4;
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            if (s.equalsIgnoreCase("-n")) {
                NUM_ITEMS = Integer.parseInt(args[i+1]); 
            } else if (s.equalsIgnoreCase("-c")) {
                COPIES_EACH = Integer.parseInt(args[i+1]);
            } else if (s.equalsIgnoreCase("-max_val")) {
                MAX_VALUE = Integer.parseInt(args[i+1]);
            } else if (s.equalsIgnoreCase("-max_weight")) {
                MAX_WEIGHT = Integer.parseInt(args[i+1]);
            } else if (s.equalsIgnoreCase("-a")){
                alg = Integer.parseInt(args[i+1]);
            } else if (s.equalsIgnoreCase("-i")){
                iters = Integer.parseInt(args[i+1]);
            } else if (s.equalsIgnoreCase("-temp")) {
                temp = Integer.parseInt(args[i+1]);
            } else if (s.equalsIgnoreCase("-decay")) {
                decay = Double.parseDouble(args[i+1]);
            } else if (s.equalsIgnoreCase("-samples")) {
                samples = Integer.parseInt(args[i+1]);
            } else if (s.equalsIgnoreCase("-toKeep")) {
                toKeep = Integer.parseInt(args[i+1]);
            } else if (s.equalsIgnoreCase("-popSize")) {
                popSize = Integer.parseInt(args[i+1]);
            } else if (s.equalsIgnoreCase("-toMutate")) {
                toMutate = Integer.parseInt(args[i+1]);
            } else if (s.equalsIgnoreCase("-toMate")) {
                toMate = Integer.parseInt(args[i+1]);
            }
        }

        MAX_KNAPSACK_WEIGHT = MAX_WEIGHT * NUM_ITEMS * COPIES_EACH * .4;

        int[] copies = new int[NUM_ITEMS];
        Arrays.fill(copies, COPIES_EACH);
        double[] values = new double[NUM_ITEMS];
        double[] weights = new double[NUM_ITEMS];
        for (int i = 0; i < NUM_ITEMS; i++) {
            values[i] = random.nextDouble() * MAX_VALUE;
            weights[i] = random.nextDouble() * MAX_WEIGHT;
        }
        int[] ranges = new int[NUM_ITEMS];
        Arrays.fill(ranges, COPIES_EACH + 1);

        EvaluationFunction ef = new KnapsackEvaluationFunction(values, weights, MAX_KNAPSACK_WEIGHT, copies);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);

        Distribution df = new DiscreteDependencyTree(.1, ranges);

        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        
        switch(alg) {
            case 0:
                RHC_funct(ef, hcp);
                break;
            case 1:
                SA_funct(ef, hcp);
                break;
            case 2:
                GA_funct(ef, odd, df, ranges);
                break;
            case 3:
                MIMIC_funct(ef, odd, df);
                break;
            case 4:
                RHC_funct(ef, hcp);
                SA_funct(ef, hcp);
                GA_funct(ef, odd, df, ranges);
                MIMIC_funct(ef, odd, df);
                break;
        }
    }

    private static void RHC_funct(EvaluationFunction ef, HillClimbingProblem hcp) {
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, iters);
        fit.train();
        // System.out.println("RHC: " + ef.value(rhc.getOptimal()));    
        System.out.println(ef.value(rhc.getOptimal()));    
    }

    private static void SA_funct(EvaluationFunction ef, HillClimbingProblem hcp) {
        SimulatedAnnealing sa = new SimulatedAnnealing(temp, decay, hcp);
        FixedIterationTrainer fit = new FixedIterationTrainer(sa, iters);
        fit.train();
        // System.out.println("SA: " + ef.value(sa.getOptimal()));
        System.out.println(ef.value(sa.getOptimal()));
    }

    private static void MIMIC_funct(EvaluationFunction ef, Distribution odd, Distribution df) {
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        MIMIC mimic = new MIMIC(samples, toKeep, pop);
        FixedIterationTrainer fit = new FixedIterationTrainer(mimic, iters);
        fit.train();
        // System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
        System.out.println(ef.value(mimic.getOptimal()));
    }

    private static void GA_funct(EvaluationFunction ef, Distribution odd, Distribution df, int[] ranges) {
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new UniformCrossOver();

        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(popSize, toMate, toMutate, gap);
        FixedIterationTrainer fit = new FixedIterationTrainer(ga, iters);
        fit.train();
        // System.out.println("GA: " + ef.value(ga.getOptimal()));
        System.out.println(ef.value(ga.getOptimal()));
    }

}
