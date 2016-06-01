package letterclassification.classification;

import letterclassification.model.Alphabet;

import java.util.*;

/**
 * Created by bradhawk on 5/26/2016.
 */
public class KNNClassifier {

    private List<Alphabet> dataTraining;
    private List<Distance> dataDistance;
    private int k;

    private double[][] dataTrainingArray;
    private double[] dataTestArray;

    public KNNClassifier(List<Alphabet> dataTraining, int k) {
        this.dataTraining = dataTraining;
        this.dataDistance = new ArrayList<>();
        this.k = k;
        refreshArray();
    }

    public void setDataTraining(List<Alphabet> dataTraining) {
        this.dataTraining = dataTraining;
        refreshArray();
    }

    public String classify(double[] dataTest) {
        refreshArray();
        this.dataTestArray = dataTest;
        standardizeTrainingData(this.dataTrainingArray, this.dataTestArray);
        return findMajorityClass(calculateDistance());
    }

    private void refreshArray() {
        int row = this.dataTraining.size();
        int column = this.dataTraining.get(0).getFeaturesAsArray().length;
        double[][] dataTraining = new double[row][column];
        for(int i = 0; i < dataTraining.length; i++) {
            for(int j = 0; j < dataTraining[i].length; j++) {
                dataTraining[i][j] = (double) this.dataTraining.get(i).getFeaturesAsArray()[j];
            }
        }
        this.dataTrainingArray = dataTraining;
    }

    private void standardizeTrainingData(double[][] dataTraining, double[] dataTest) {
        double[][] processingData = new double[dataTraining.length + 1][dataTraining[0].length];
        this.dataTrainingArray = new double[dataTraining.length][dataTraining[0].length];
        this.dataTestArray = new double[dataTest.length];

        for (int i = 0; i < dataTraining.length; i++) {
            System.arraycopy(dataTraining[i], 0, processingData[i], 0, dataTraining[i].length);
        }
        System.arraycopy(dataTest, 0, processingData[processingData.length - 1], 0, dataTest.length);

        for(int i = 0; i < processingData[0].length; i++) {
            double max = processingData[0][i];
            double min = processingData[0][i];
            for(int j = 1; j < processingData.length; j++) {
                if(processingData[j][i] > max) {
                    max = processingData[j][i];
                }
                if(processingData[j][i] < min) {
                    min = processingData[j][i];
                }
            }
            for(int j = 0; j < processingData.length; j++) {
                processingData[j][i] = (processingData[j][i] - min) / (max - min);
            }
        }

        for(int i = 0; i < dataTraining.length; i++) {
            System.arraycopy(processingData[i], 0, this.dataTrainingArray[i], 0, dataTraining[i].length);
        }
        System.arraycopy(processingData[processingData.length - 1], 0, this.dataTestArray, 0, dataTest.length);
    }

    private Alphabet[] calculateDistance() {
        dataDistance.clear();
        for(int i = 0; i < this.dataTrainingArray.length; i++) {
            double distance = 0.0;
            for(int j = 0; j < this.dataTrainingArray[i].length; j++) {
                distance += Math.pow(this.dataTrainingArray[i][j] - this.dataTestArray[j], 2);
            }
            distance = Math.sqrt(distance);
            dataDistance.add(new Distance(dataTraining.get(i), distance));
        }
        Collections.sort(dataDistance, new DistanceComparator());
        Alphabet[] selectedClass = new Alphabet[this.k];
        for(int i = 0; i < selectedClass.length; i++) {
            selectedClass[i] = dataDistance.get(i).alphabet;
            System.out.println("Selected Class = " + dataDistance.get(i).alphabet.getLetter() + " with Distance = " + dataDistance.get(i).distance);
        }
        return selectedClass;
    }

    private String findMajorityClass(Alphabet[] selectedDistance) {
        String[] classAlphabet = new String[selectedDistance.length];
        for(int i = 0; i < classAlphabet.length; i++) {
            classAlphabet[i] = selectedDistance[i].getLetter();
        }
        Set<String> h = new HashSet<>(Arrays.asList(classAlphabet));
        String[] uniqueValues = h.toArray(new String[0]);
        int[] counts = new int[uniqueValues.length];
        for (int i = 0; i < uniqueValues.length; i++) {
            for (int j = 0; j < classAlphabet.length; j++) {
                if(classAlphabet[j].equals(uniqueValues[i])){
                    counts[i]++;
                }
            }
        }
        int max = counts[0];
        for(int i = 0; i < counts.length; i++) {
            if(counts[i] > max) {
                max = counts[i];
            }
        }

        int freqMaxShow = 0;
        for(int i = 0; i < counts.length; i++) {
            if(counts[i] == max) {
                freqMaxShow++;
            }
        }

        int index = -1;
        if(freqMaxShow == 1) {
            for(int i = 0; i < counts.length; i++) {
                if(counts[i] == max) {
                    index = i;
                    break;
                }
            }
            return uniqueValues[index];
        } else {
            int[] indexSave = new int[freqMaxShow];
            int indexCounter = 0;
            for(int i = 0; i < counts.length; i++) {
                if(counts[i] == max) {
                    indexSave[indexCounter] = i;
                    indexCounter++;
                }
            }

            Random randomNumber = new Random();
            int randomizedIndex = randomNumber.nextInt(indexSave.length);
            int selectedIndex = indexSave[randomizedIndex];
            return uniqueValues[selectedIndex];
        }
    }

    private class Distance {
        Alphabet alphabet;
        double distance;

        Distance(Alphabet alphabet, double distance) {
            this.alphabet = alphabet;
            this.distance = distance;
        }
    }

    private class DistanceComparator implements Comparator<Distance> {

        @Override
        public int compare(Distance object1, Distance object2) {
            return object1.distance < object2.distance ? -1 : object1.distance == object2.distance ? 0 : 1;
        }
    }

}
