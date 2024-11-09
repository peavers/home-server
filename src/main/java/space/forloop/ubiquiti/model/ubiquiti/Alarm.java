package space.forloop.ubiquiti.model.ubiquiti;

@lombok.Data
public class Alarm {
    private String name;
    private Object[] sources;
    private ConditionElement[] conditions;
    private Trigger[] triggers;
}
