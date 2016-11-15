package jdblender.cayenne.model;

import jdblender.cayenne.model.auto._Spares;
import jdblender.core.domain.Spare;
import jdblender.core.domain.SpareObj;

public class Spares extends _Spares implements Spare, SpareObj {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean getFlag() {
        return isFlag();
    }
}
