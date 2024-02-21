package lab2;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MyAlg {
    static int dimension = 100; // dimension of problem
    static int populationSize = 100; // size of population
    static int generations = 10000; // number of generations

    public static void main(String[] args) {
        ArrayList<Double> scores = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            scores.add(run());
        }
        System.out.println(scores);
        System.out.println(scores.stream().mapToDouble(a -> a).average());
        System.out.println(scores.stream().max(Double::compare));
        System.out.println((scores.stream().max(Double::compare).orElse(0.) + scores.stream().mapToDouble(a -> a).average().orElse(0.))/2);
    }

    public static double run() {
        final double[] bestFit = {0};
        //7.24798467519943
        Random random = new Random(); // random

        CandidateFactory<double[]> factory = new MyFactory(dimension); // generation of solutions

        ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
        operators.add(new MyCrossover()); // Crossover
        operators.add(new MyMutation()); // Mutation
        EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

        SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

        FitnessEvaluator<double[]> evaluator = new FitnessFunction(dimension); // Fitness function

        EvolutionEngine<double[]> algorithm = new SteadyStateEvolutionEngine<double[]>(
                factory, pipeline, evaluator, selection, populationSize, false, random);

        algorithm.addEvolutionObserver(new EvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                bestFit[0] = populationData.getBestCandidateFitness();
                System.out.println("Generation " + populationData.getGenerationNumber() + ": " + bestFit[0]);
//                System.out.println("\tBest solution = " + Arrays.toString((double[]) populationData.getBestCandidate()));
//                System.out.println("\tPop size = " + populationData.getPopulationSize());
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);
        algorithm.evolve(populationSize, 1, terminate);
        return bestFit[0];
    }

    public static void findParams() {

    }
}
