package world.entity.execution;
import world.property.Property;
import java.io.Serializable;
import java.util.*;

public class EntityInstance  implements Serializable {
    private final boolean ENTITY_ALIVE = true;
    private final int DEFAULT_VALUE = -1;

    private String entityName;
    private Map<String,Property> entityProperties;
    private static int lastAssignedId = 0;
    private int id;
    private boolean isAlive;
    private int row;
    private int col;


    public EntityInstance(String entityName) {
        this.entityProperties = new LinkedHashMap<>();
        lastAssignedId++;
        this.id=lastAssignedId;
        this.entityName=entityName;
        this.isAlive=ENTITY_ALIVE;
        this.row=DEFAULT_VALUE;
        this.col=DEFAULT_VALUE;
    }
    public void randomPlaceInGrid(EntityInstance[][]grid){
        boolean isNewPlaceSet=false;
        Random random = new Random();
        List<Integer>optionsList=new ArrayList<>();
        optionsList.add(1);
        optionsList.add(2);
        optionsList.add(3);
        optionsList.add(4);
        int maxRows = grid.length;
        int maxColumns = grid[0].length;
        while (!isNewPlaceSet){
            int randomIndex = random.nextInt(optionsList.size());
            int randomValue = optionsList.remove(randomIndex);
            switch (randomValue) {
                case (1):
                    if (this.row + 1 == maxRows) {
                        if (grid[0][this.col] == null) {
                            grid[0][this.col] = this;
                            grid[this.row][this.col] = null;
                            this.row = 0;
                            isNewPlaceSet = true;
                            break;
                        }
                    } else if (grid[this.row + 1][this.col] == null) {
                        grid[this.row + 1][this.col] = this;
                        grid[this.row][this.col] = null;
                        this.row = this.row + 1;
                        isNewPlaceSet = true;
                        break;
                    }
                    break;
                case (2):
                {
                    if (this.row == 0) {
                        if (grid[maxRows - 1][this.col] == null) {
                            grid[maxRows - 1][this.col] = this;
                            grid[this.row][this.col] = null;
                            this.row = maxRows - 1;
                            isNewPlaceSet = true;
                            break;
                        }
                    } else if (grid[this.row - 1][this.col] == null) {
                        grid[this.row - 1][this.col] = this;
                        grid[this.row][this.col] = null;
                        this.row = this.row - 1;
                        isNewPlaceSet = true;
                        break;
                    }
            }
                    break;
                case (3):
                    if(this.col+1==maxColumns)
                    {
                        if(grid[this.row][0]==null){
                            grid[this.row][0]=this;
                            grid[this.row][this.col]=null;
                            this.col=0;
                            isNewPlaceSet=true;
                            break;
                        }
                    }
                    else if(grid[this.row][this.col+1]==null){
                        grid[this.row][this.col+1]=this;
                        grid[this.row][this.col]=null;
                        this.col=this.col+1;
                        isNewPlaceSet=true;
                        break;
                    }
                    break;
                case (4):
                    if(this.col==0)
                    {
                        if(grid[this.row][maxColumns-1]==null){
                            grid[this.row][maxColumns-1]=this;
                            grid[this.row][this.col]=null;
                            this.col=maxColumns-1;
                            isNewPlaceSet=true;
                            break;
                        }
                    }
                    else if(grid[this.row][this.col-1]==null){
                        grid[this.row][this.col-1]=this;
                        grid[this.row][this.col]=null;
                        this.col=this.col-1;
                        isNewPlaceSet=true;
                        break;
                    }
                    break;
            }
            if(optionsList.isEmpty()){
                isNewPlaceSet=true;
            }


        }
    }


    public boolean isEntityAlive() {
        return this.isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Map<String, Property> getEntityProperties() {
        return entityProperties;
    }

    public int getId() {
        return id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getNameOfEntity() {
        return entityName;
    }

    public void resetId() {
        lastAssignedId=0;
        this.id=lastAssignedId;
    }
    public void addProperty(Property newProperty) {
        entityProperties.put(newProperty.getPropertyName(),newProperty);
    }

    @Override
    public String toString() {
        StringBuilder result  = new StringBuilder();
        if (!(this.entityProperties.isEmpty()))
        {
            result.append("Properties details:\n");
            int i=1;
            for (Property property : this.entityProperties.values())
            {
                result.append("(").append(i).append(") ").append(property.toString()).append("\n");
                i++;
            }
        }
        return result.toString();
    }
}
