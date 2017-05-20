package it.polimi.ingsw.pcXX;

/**
 * Created by trill on 20/05/2017.
 */
public class ActionModifier {
    private  ActionType type;
    private int modifier;

    public ActionModifier(ActionType type, int modifier){
        this.type = type;
        this.modifier = modifier;
    }

    public ActionModifier(ActionType type){
        this(type, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionModifier that = (ActionModifier) o;

        if (modifier != that.modifier) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + modifier;
        return result;
    }

    @Override
    public String toString() {
        return "" + type.toString() + ": " + modifier;
    }

    public void varyModifier(int vary){
        modifier += vary;
    }
}
