package it.polimi.ingsw.model;

import it.polimi.ingsw.model.branch.Branch;

import java.util.List;

public interface Branchable {

    public List<Branch> getBranches(Player branchesOwner);
}
