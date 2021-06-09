package entities;

public class Category {

    private int id;
    private String name,
            description;
    private boolean isActive;
    private int order;

    public Category() {
    }

    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Category(int id, String name, String description, boolean isActive, int order) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.order = order;
    }


    
    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public int getOrder() {
        return order;
    }
     
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    public void setOrder(int order) {
        this.order = order;
    }
      
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
