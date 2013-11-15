package share.manager.utils;

public enum GraphType {
    BAR("Bar"),
    LINEAR("Linear"),
    CIRCULAR("Circular");

    private final String name;       

    private GraphType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    @Override
	public String toString(){
       return name;
    }

}