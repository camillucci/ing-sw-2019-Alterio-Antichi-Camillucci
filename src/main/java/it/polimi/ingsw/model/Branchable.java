package it.polimi.ingsw.model;

import java.util.List;

public interface Branchable {

    public List<Branch> getBranches(Player branchesOwner);
}
