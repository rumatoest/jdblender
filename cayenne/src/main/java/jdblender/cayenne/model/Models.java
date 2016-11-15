package jdblender.cayenne.model;

import jdblender.cayenne.model.auto._Models;
import jdblender.core.domain.Model;
import jdblender.core.domain.ModelObj;
import jdblender.core.domain.SpareObj;

import java.util.List;

public class Models extends _Models implements Model, ModelObj {

    private static final long serialVersionUID = 1L;

    @Override
    public List<? extends SpareObj> getSpares() {
        return null;
    }
}
