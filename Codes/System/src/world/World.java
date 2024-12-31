package world;

import exception.ValueExists;
import simulation.SimulationExecutionDetails;
import world.entity.definition.EntityDefinition;
import world.entity.execution.EntityInstance;
import world.environment.Environment;
import world.rule.management.Rules;

import java.io.*;
import java.util.*;

public class World implements Serializable {
    private Map<String, EntityDefinition> entities;
    private Rules rules;
    private Environment environment;
    private Integer ticksCount;
    private Integer secondsCount;
    private Integer dimensionSize;
    private Integer threadPoolSize;
    private Integer rows;
    private Integer columns;
    private IntPair attractiveLock=new IntPair(-1,-1);

    private SimulationExecutionDetails simulationExecutionDetails;
    private EntityInstance[][] gridArray;
    private boolean stop;
    private boolean isUserTermination;
    private boolean continueForce;
    private boolean pause;
    private List<IntPair> emptyGridPlaces;

    public World(Rules rules, Environment environment, Integer ticksCount, Integer secondsCount, Map<String, EntityDefinition> entities, int rows, int columns, int threadPoolSize,EntityInstance[][] gridArray) {
        this.entities = new LinkedHashMap<>();
        this.emptyGridPlaces=new ArrayList<>();
        this.entities.putAll(entities);
        this.rules = rules;
        this.environment = environment;
        this.secondsCount = secondsCount;
        this.ticksCount = ticksCount;
        this.threadPoolSize = threadPoolSize;
        isUserTermination = secondsCount == null && ticksCount == null;
        this.dimensionSize = rows * columns;
        this.rows = rows;
        this.columns = columns;
        for (int row=0;row<this.rows;row++) {
            for (int col = 0; col < this.columns; col++) {
                this.emptyGridPlaces.add(new IntPair(row,col));
            }
        }
        this.gridArray=gridArray;
    }

    public void setSimulationExecutionDetails(SimulationExecutionDetails simulationExecutionDetails) {
        this.simulationExecutionDetails = simulationExecutionDetails;
    }

    public SimulationExecutionDetails getSimulationExecutionDetails() {
        return simulationExecutionDetails;
    }

    public Map<String, EntityDefinition> getEntities() {
        return entities;
    }

    public Integer getTicksCount() {
        return ticksCount;
    }

    public List<String> getTermination(){
        List<String>res=new ArrayList<>();
        if(secondsCount==null&&ticksCount==null)
        {
            res.add("End by user decision");
            return res;
        }
        if(ticksCount!=null){
            res.add("By ticks : "+String.valueOf(ticksCount));
        }
        if(secondsCount!=null){
            res.add("By seconds : "+String.valueOf(secondsCount));
        }

        return res;
    }
    public Integer getSecondsCount() {
        return secondsCount;
    }

    public Integer getThreadPoolSize() {
        return threadPoolSize;
    }

    public EntityInstance[][] getGridArray() {
        return gridArray;
    }

    public void addInstanceToGrid(EntityInstance currInstance) {

        Random random = new Random();
        int randomIndex = random.nextInt( this.emptyGridPlaces.size());
        IntPair randomElement =  this.emptyGridPlaces.remove(randomIndex);
        this.gridArray[randomElement.first][randomElement.second]=currInstance;
        currInstance.setRow(randomElement.first);
        currInstance.setCol(randomElement.second);
    }

    public Rules getRules() {
        return rules;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Integer getDimensionSize() {
        return dimensionSize;
    }
    public void simulationStop(){
        stop=true;
        synchronized (attractiveLock){
            pause=false;
            continueForce=false;
            attractiveLock.notifyAll();
        }
    }
    public void simulationPause(){
            pause=true;
    }
    public void simulationContinue(){
        synchronized (attractiveLock){
            pause=false;
            continueForce=true;
            attractiveLock.notifyAll();
        }
    }
    public Object getAttractiveLock() {
        return attractiveLock;
    }


    public boolean isStop() {
        return stop;
    }

    public boolean isPause() {
        return pause;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (EntityDefinition entityDefinition : entities.values()) {
            builder.append(entityDefinition.toString()).append("\n");
        }
        builder.append(rules.toString());
        if (ticksCount != null && secondsCount != null) {
            builder.append("Ending terms: ticks = ").append(this.ticksCount).append(" , seconds = ").append(this.secondsCount).append("\n\n");
        } else if (ticksCount != null) {
            builder.append("Ending terms: ticks = ").append(this.ticksCount).append("\n\n");
        } else if (secondsCount != null) {
            builder.append("Ending terms: seconds = ").append(this.secondsCount).append("\n\n");
        }

        return builder.toString();
    }

    public World deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            return (World) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    class IntPair  implements Serializable{
        int first;
        int second;

        public IntPair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }
}
