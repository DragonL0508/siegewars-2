package me.dragonl.siegewars.game.kit;

import io.fairyproject.command.CommandContext;
import io.fairyproject.command.exception.ArgTransformException;
import io.fairyproject.command.parameter.ArgTransformer;
import io.fairyproject.container.Autowired;
import io.fairyproject.container.InjectableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@InjectableComponent
public class KitArgTransformer implements ArgTransformer<SiegeWarsKit> {
    @Autowired
    private KitManager kitManager;

    @Override
    public Class[] type() {
        return new Class[]{SiegeWarsKit.class};
    }

    @Override
    public SiegeWarsKit transform(CommandContext commandContext, String kitID) throws ArgTransformException {
        SiegeWarsKit kit = kitManager.getKit(kitID);
        if (kit == null) {
            throw new ArgTransformException("Kit not found: " + kitID);
        }
        return kit;
    }

    @Override
    public List<String> tabComplete(CommandContext commandContext, String source) throws ArgTransformException {
        return kitManager.getKits().keySet().stream().filter(id -> id.startsWith(source)).collect(Collectors.toList());
    }
}
