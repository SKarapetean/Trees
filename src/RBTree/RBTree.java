package RBTree;

import java.util.LinkedList;
import java.util.Queue;

public class RBTree<T extends Comparable<T>> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private Node<T> root;

    public RBTree (T item) {
        this.insert(item);
    }

    public RBTree () {}
    private static class Node<T> {
        T item;
        Node<T> parent;
        Node<T> left;
        Node<T> right;
        boolean color;

        Node () { }
        Node (T item) {
            this.item = item;
            this.color = RED;
        }

        public String toString () {
            return item.toString() + " " + (color ? "BLACK" : "RED");
        }
    }

    private static class Nil<T> extends Node<T> {
        Nil () {
            super();
            this.color = BLACK;
        }
    }

    public boolean search (T item) {
        Node<T> node = root;
        while (node != null) {
            if (node.item.compareTo(item) == 0) {
                return true;
            }
            if (node.item.compareTo(item) > 0) {
                node = node.left;
            } else if (node.item.compareTo(item) < 0) {
                node = node.right;
            }
        }
        return false;
    }


    private void rotateRight (Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> leftChild = node.left;

        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.right = node;
        node.parent = leftChild;

        replaceParentChild(parent, node, leftChild);
    }

    private void rotateLeft (Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> rightChild = node.right;

        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.left = node;
        node.parent = rightChild;

        replaceParentChild(parent, node, rightChild);
    }

    private void replaceParentChild (Node<T> parent, Node<T> oldChild, Node<T> newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else if (parent.right == oldChild) {
            parent.right = newChild;
        } else {
            throw new IllegalStateException("replaceParentChild");
        }

        if (newChild != null) {
            newChild.parent = parent;
        }
    }
    public void insert (T item){
        insert(item, root);
    }

    private void insert (T item, Node<T> root) {
        Node<T> parent = null;

        while (root != null) {
            parent = root;
            if (item.compareTo(root.item) < 0) {
                root = root.left;
            } else {
                root = root.right;
            }
        }

        Node<T> newNode = new Node<>(item);
        if (parent == null) {
            this.root = newNode;
        } else if (item.compareTo(parent.item) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;

        fixPropertiesAfterInsertion(newNode);
    }

    private Node<T> getUncle (Node<T> parent) {
        Node<T> granny = parent.parent;
        if (granny.left == parent) {
            return granny.right;
        }
        if (granny.right == parent) {
            return granny.left;
        }

        return null;
    }

    private void fixPropertiesAfterInsertion (Node<T> node) {
        Node<T> parent = node.parent;

        if (parent == null) {
            node.color = BLACK;
            return;
        }

        if (parent.color == BLACK) {
            return;
        }

        Node<T> granny = parent.parent;
        if(granny == null) {
            parent.color = BLACK;
            return;
        }

        Node<T> uncle = this.getUncle(parent);
        if (uncle != null && uncle.color == RED) {
            parent.color = BLACK;
            uncle.color = BLACK;
            granny.color = RED;
            fixPropertiesAfterInsertion(granny);
        } else if (parent == granny.left) {
            if (node == parent.right) {
                rotateLeft(parent);
                parent = node;
            }
            rotateRight(granny);
            parent.color = BLACK;
            granny.color = RED;

        } else {
            if (node == parent.left) {
                rotateRight(parent);
                parent = node;
            }
            rotateLeft(granny);
            parent.color = BLACK;
            granny.color = RED;
        }

    }

    public void delete (T item) {
        delete(item, root);
    }

    private void delete (T item, Node<T> node) {
        while (node != null && !node.item.equals(item)) {
            if (item.compareTo(node.item) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (node == null) {
            return;
        }

        Node<T> moveUpNode;
        boolean deletedNodeColor;

        if (node.left == null || node.right == null) {
            deletedNodeColor = node.color;
            moveUpNode = deleteNodeWithZeroOrOneChild(node);
        } else {
            Node<T> successor = findSuccessorNode(node);
            node.item = successor.item;
            deletedNodeColor = successor.color;
            moveUpNode = deleteNodeWithZeroOrOneChild(successor);
        }

        if (deletedNodeColor == BLACK) {
            fixPropertiesAfterDeletion(moveUpNode);
            if (moveUpNode.getClass() == Nil.class) {
                replaceParentChild(moveUpNode.parent, moveUpNode, null);
            }
        }
    }

    private Node<T> deleteNodeWithZeroOrOneChild (Node<T> node) {
        if (node.left != null) {
            replaceParentChild(node.parent, node, node.left);
            return node.left;
        } else if (node.right != null) {
            replaceParentChild(node.parent, node, node.right);
            return node.right;
        } else {
            Node<T> newChild = node.color == BLACK ? new Nil<>() : null;
            replaceParentChild(node.parent, node, newChild);
            return newChild;
        }
    }

    private void fixPropertiesAfterDeletion (Node<T> node) {
        if (node == root) {
            node.color = BLACK;
            return;
        }

        Node<T> sibling = getSibling(node);
        if (sibling.color == RED) {
            handleRedSibling(node, sibling);
            sibling = getSibling(node);
        }
        if (isBlack(sibling.left) && isBlack(sibling.right)) {
            sibling.color = RED;

            if (node.parent.color == RED) {
                node.parent.color = BLACK;
            } else {
                fixPropertiesAfterDeletion(node.parent);
            }
        } else {
            handleBlackSiblingWithAtLeastOneRedChild(node, sibling);
        }

    }

    private Node<T> getSibling (Node<T> node) {
        Node<T> parent = node.parent;
        if (node == parent.left) {
            return parent.right;
        }

        return parent.left;
    }

    private void handleRedSibling (Node<T> node, Node<T> sibling) {
        sibling.color = BLACK;
        sibling.parent.color = RED;

        if (sibling == sibling.parent.right) {
            rotateLeft(sibling.parent);
        } else {
            rotateRight(sibling.parent);
        }
    }

    private void handleBlackSiblingWithAtLeastOneRedChild (Node<T> node, Node<T> sibling) {
        boolean nodeIsLeftChild = node == node.parent.left;

        if (nodeIsLeftChild && isBlack(sibling.right)) {
            sibling.left.color = BLACK;
            sibling.color = RED;
            rotateRight(sibling);
            sibling = node.parent.right;
        } else if (!nodeIsLeftChild && isBlack(sibling.left)) {
            sibling.right.color = BLACK;
            sibling.color = RED;
            rotateLeft(sibling);
            sibling = node.parent.left;
        }

        sibling.color = node.parent.color;
        node.parent.color = BLACK;
        if (nodeIsLeftChild) {
            sibling.right.color = BLACK;
            rotateLeft(node.parent);
        } else {
            sibling.left.color = BLACK;
            rotateRight(node.parent);
        }
    }

    private boolean isBlack (Node<T> node) {
        return node == null || node.color == BLACK;
    }

    private Node<T> findSuccessorNode (Node<T> root) {
        if (root == null) {
            return null;
        }
        if (root.right == null) {
            return null;
        }

        return findMinNode(root.right);
    }

    private Node<T> findPredecessorNode (Node<T> root) {
        if (root == null) {
            return null;
        }

        if (root.left == null) {
            return null;
        }

        return findMaxNode(root.left);
    }

    private Node<T> findMinNode (Node<T> root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    private Node<T> findMaxNode (Node<T> root) {
        while (root.right != null) {
            root = root.right;
        }
        return root;
    }

//    public void levelOrderTraversal() {
//        if (root == null) {
//            return;
//        }
//
//        Queue<Node<T>> queue = new LinkedList<>();
//        queue.add(root);
//
//        while (!queue.isEmpty()) {
//            Node<T> node = queue.poll();
//            System.out.println(node);
//            if (node.left != null) {
//                queue.add(node.left);
//            }
//            if (node.right != null) {
//                queue.add(node.right);
//            }
//
//        }
//    }

    public void levelOrderTraversal() {
        int height = getHeight(root);

        for (int i = 1; i <= height; ++i) {
            printLevel(root, i);
            System.out.println();
        }
    }

    private int getHeight(Node<T> root) {
        if (root == null) {
            return 0;
        }

        int leftHeight = getHeight(root.left);
        int rightHeight = getHeight(root.right);

        return Math.max(leftHeight, rightHeight) + 1;
    }

    private void printLevel(Node<T> root, int level) {
        if (root == null) {
            return;
        }

        if (level == 1) {
            System.out.print(root + "   ");
        } else if (level > 1) {
            printLevel(root.left, level - 1);
            printLevel(root.right, level - 1);
        }
    }

}
