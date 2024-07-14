/**
 * The Tree class represents a tree structure with integer data.
 * Each node can have multiple children, and the nodes store a 2D integer array as data.
 * The score attribute represents a numerical value associated with the tree.
 */

package tree;

import java.util.ArrayList;

public class Tree {

    // Data stored in the node
    public int[][] data;

    // List of children nodes
    public ArrayList<Tree> children;

    // Score associated with the tree
    public int score;

    // Number of children nodes
    private int nbChildren;

    /**
     * Constructor for the Tree class.
     *
     * @param data The 2D integer array to be stored in the tree node.
     */
    public Tree(int[][] data) {
        this.data = data;
        this.children = new ArrayList<Tree>();
        this.nbChildren = 0;
        this.score = 0;
    }

    /**
     * Adds a child to the current tree node.
     *
     * @param childData The 2D integer array to be stored in the new child node.
     * @return The newly added child node.
     */
    public Tree addChild(int[][] childData) {
        Tree newChild = new Tree(childData);
        this.children.add(newChild);
        this.nbChildren++;
        return newChild;
    }

    /**
     * Returns the number of children nodes.
     *
     * @return The number of children nodes.
     */
    public int nbChildren() {
        return this.nbChildren;
    }

    /**
     * Removes a specified child node from the current tree node.
     *
     * @param toRemove The child node to be removed.
     * @return True if the removal was successful, false otherwise.
     */
    public boolean removeChild(Tree toRemove) {
        if (this.children.remove(toRemove)) {
            this.nbChildren--;
            return true;
        }
        return false;
    }

    /**
     * Removes a child node with a specific 2D integer array data from the current tree node.
     *
     * @param data The 2D integer array data to be matched for removal.
     * @return The removed child node if found, null otherwise.
     */
    public Tree removeChildWithData(int[][] data) {
        for (int i = 0; i < this.nbChildren(); i++) {
            if (this.children.get(i).data == data) {
                Tree deleted = this.children.remove(i);
                if (deleted != null)
                    this.nbChildren--;
                return deleted;
            }
        }
        return null;
    }
}
