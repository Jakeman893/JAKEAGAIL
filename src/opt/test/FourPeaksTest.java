package opt.test;

import java.util.Arrays;

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
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class FourPeaksTest {
    /** The n value */
    private static int N = 200;
    /** The t value */
    private static int T = N / 5;
    
    public static void main(String[] args) {
        int alg = 4;
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            if (s.equalsIgnoreCase("-t")) {
                T = Integer.parseInt(args[i+1]); 
            } else if (s.equalsIgnoreCase("-N")) {
                N = Integer.parseInt(args[i+1]);
            } else if (s.equalsIgnoreCase("-a")){
                alg = Integer.parseInt(args[i+1]);
            }
        }
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        EvaluationFunction ef = new FourPeaksEvaluationFunction(T);
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
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 200000);
        fit.train();
        // System.out.println("RHC: " + ef.value(rhc.getOptimal()));    
        System.out.println(ef.value(rhc.getOptimal()));    
    }

    private static void SA_funct(EvaluationFunction ef, HillClimbingProblem hcp) {
        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
        FixedIterationTrainer fit = new FixedIterationTrainer(sa, 200000);
        fit.train();
        // System.out.println("SA: " + ef.value(sa.getOptimal()));
        System.out.println(ef.value(sa.getOptimal()));
    }

    private static void MIMIC_funct(EvaluationFunction ef, Distribution odd, Distribution df) {
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        MIMIC mimic = new MIMIC(200, 20, pop);
        FixedIterationTrainer fit = new FixedIterationTrainer(mimic, 1000);
        fit.train();
        // System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));
        System.out.println(ef.value(mimic.getOptimal()));
    }

    private static void GA_funct(EvaluationFunction ef, Distribution odd, Distribution df, int[] ranges) {
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new SingleCrossOver();

        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
        FixedIterationTrainer fit = new FixedIterationTrainer(ga, 1000);
        fit.train();
        // System.out.println("GA: " + ef.value(ga.getOptimal()));
        System.out.println(ef.value(ga.getOptimal()));
    }

}
