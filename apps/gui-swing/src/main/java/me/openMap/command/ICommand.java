package me.openMap.command;

import java.awt.event.ActionListener;
import java.util.List;

import me.openMap.OpenTransitMap;

public interface ICommand extends ActionListener{

    public void initilize(OpenTransitMap app);
    public void paramenters(List<String> parameters);
}
