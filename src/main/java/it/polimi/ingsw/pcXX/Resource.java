package it.polimi.ingsw.pcXX;

public class Resource extends Reward{
	private final ResourceType type;
	private int quantity;

	public Resource(ResourceType type, int quantity){
		this.type = type;
		this.quantity = quantity;
	}

    public int getQuantity() {
        return quantity;
    }

	public ResourceType getType() {
		return type;
	}

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Resource resource = (Resource) o;

		if (quantity != resource.quantity) return false;
		return type == resource.type;
	}

	@Override
	public int hashCode(){
		int result = type.hashCode();
		result = 31 * result + quantity;
		return result;
	}

	@Override
	public String toString(){
		return "" + quantity + " " + type.toString();
	}

	public void addQuantity(int var){
		quantity += var;
	}
}
