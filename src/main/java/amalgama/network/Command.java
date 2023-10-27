package amalgama.network;

import amalgama.utils.TypeUtils;

public class Command {
    public String src;
    public Type type;
    public String[] args;

    public Command(String data) {
        src = data;
        args = data.split(";");
        type = TypeUtils.typeOf(args[0]);
    }
}
