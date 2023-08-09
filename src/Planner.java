import java.util.ArrayList;
import java.util.Collections;

public class Planner {

    public final Task[] taskArray;
    public final Integer[] compatibility;
    public final Double[] maxWeight;
    public final ArrayList<Task> planDynamic;
    public final ArrayList<Task> planGreedy;

    public Planner(Task[] taskArray) {

        this.taskArray = taskArray;
        this.compatibility = new Integer[taskArray.length];
        maxWeight = new Double[taskArray.length];

        this.planDynamic = new ArrayList<>();
        this.planGreedy = new ArrayList<>();
    }

    public int binarySearch(int index) {
        int low =0, high = taskArray.length-1,compatible = -1, mid;
        while(high- low >= 0){
            mid = ( low+high ) / 2;
            Task inTask =taskArray[index];
            //If the finish time of one task is less than or equal to the start time of the other task, this task is compatible with the other task.
            // for this approach, we assume that tasks are already in ascending order of finish time
            if(taskArray[mid].getFinishTime().compareTo(inTask.getStartTime()) <=0){
                compatible=mid;
                low = mid+1;

            }
            else{
                high= mid-1 ;
            }
        }
        return compatible;
    }
    public void calculateCompatibility() {
        // find compatible tasks by using binary search instead of brute-force approach to increase efficiency
        for(int i =0; i < taskArray.length; i++){
            compatibility[i]= binarySearch(i);
        }
    }
    public ArrayList<Task> planDynamic() {
        calculateCompatibility(); // it is needed to know which tasks can be planned without conflicts
        System.out.println("Calculating max array");
        System.out.println("---------------------");
        calculateMaxWeight(taskArray.length-1);
        System.out.println();
        System.out.println("Calculating the dynamic solution");
        System.out.println("--------------------------------");
        solveDynamic(taskArray.length-1);
        System.out.println();
        System.out.println("Dynamic Schedule");
        System.out.println("----------------");
        Collections.reverse(planDynamic);
        for( Task pd :planDynamic){
            System.out.println("At " + pd.getStartTime() + ", " + pd.getName() + ".");
        }
        return planDynamic;
    }
    public void solveDynamic(int i) {
        // This method is implemented by using below logic
        // FIND-SOLUTION( j )
        //     IF (j = 0) RETURN ∅.
        //     ELSE IF (wj + M[p[j]] > M[j–1]) RETURN { j } ∪ FIND-SOLUTION(p[ j ]).
        //     ELSE
        //        RETURN FIND-SOLUTION( j – 1)

        if(i== -1){return;  }
        System.out.println("Called solveDynamic("+ i +")");
        if(i== 0){
            planDynamic.add(taskArray[i]);
        }
        // since our compatibility array includes -1 instead of 0 for empty and there cannot be an array with an index of -1, I checked also taskArray[i].getWeight()> maxWeight[i-1] to prevent error
        else if (taskArray[i].getWeight()> maxWeight[i-1] || taskArray[i].getWeight()+ maxWeight[compatibility[i]] >maxWeight[i -1]) {
            planDynamic.add(taskArray[i]);
            solveDynamic(compatibility[i]);
        }
        else{
            solveDynamic(i-1);
        }

    }

    /* This function calculates maximum weights and prints out whether it has been called before or not  */
    public Double calculateMaxWeight(int i) {
        // base case
        if( i==-1){
            System.out.println("Called calculateMaxWeight(" + i + ")");
            return 0.0;
        }
        // if M[j] is not null return M[j]
        if (maxWeight[i] !=null && i!=0){
            System.out.println("Called calculateMaxWeight(" + i + ")");
            return maxWeight[i];
        }

        System.out.println("Called calculateMaxWeight(" + i + ")");
        // otherwise M[j] ← max { M-COMPUTE-OPT (j – 1), wj + M-COMPUTE-OPT(p[j]) } (memoization is used, not brute-force)
        Double caseOne=taskArray[i].getWeight() +calculateMaxWeight(compatibility[i]);
        Double caseTwo =calculateMaxWeight(i-1);
        maxWeight[i]= Math.max(caseOne,caseTwo);
        return maxWeight[i];
    }

    /*
     * This function is for generating a plan using the greedy approach.
     * */
    public ArrayList<Task> planGreedy() {
        // This method is implemented by using logic which is greedy-earliest finish time first

        System.out.println("Greedy Schedule");
        System.out.println("---------------");
        planGreedy.add(taskArray[0]);

        System.out.println("At " + taskArray[0].getStartTime() + ", " + taskArray[0].getName() + ".");
        for( int i =1; i< taskArray.length; i++){
            //check if the task is compatible with the most recently selected task, if it is compatible add it to plan greedy
            if( taskArray[i].getStartTime().compareTo(planGreedy.get(planGreedy.size() -1).getFinishTime()) >=0){
                planGreedy.add(taskArray[i]);
                System.out.println("At " + taskArray[i].getStartTime() + ", " + taskArray[i].getName() + ".");

            }
        }
        return planGreedy;
    }
}
