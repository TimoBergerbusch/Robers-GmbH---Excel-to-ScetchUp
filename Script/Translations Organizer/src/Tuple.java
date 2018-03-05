/**
 * Created by Timo Bergerbusch on 17.02.2018.
 */
class Tuple {

    private final int index;
    private final Requirement requirement;

    public Tuple(int index, Requirement name) {
        this.index = index;
        this.requirement = name;
    }

    public int getIndex() {
        return index;
    }

    public Requirement getRequirement(){
        return requirement;
    }

    public String getRequirementName() {
        return requirement.getName();
    }

    public String toString() {
        return "(" + index + "," + requirement.getName() + ")";
    }
}
