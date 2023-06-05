package AVL;

import java.util.ArrayList;
import java.util.List;

public class AVL<T extends Comparable<T>> {

    private Node<T> root;

    private int nodesCount;

    public AVL(){}

    public AVL(T root) {
        this.root = new Node<>(root, 1);
    }

    private static class Node<T> {
        T item;
        Node<T> left;
        Node<T> right;
        int height;
        Node(T item) {
            this.item = item;
        }
        Node(T item, int height) {
            this.item = item;
            this.height = height;
        }

        public String toString() {
            return item.toString();
        }
    }

    public void insert(T data) {
        this.root = this.insert(root, data);
    }

    private Node<T> insert(Node<T> root, T data) {
        if (root == null) {
            ++this.nodesCount;
            return new Node<>(data);
        }

        if (data.compareTo(root.item) > 0) {
            root.right = insert(root.right, data);
        } else if (data.compareTo(root.item) < 0){
            root.left = insert(root.left, data);
        } else {
            return root;
        }

        return reBalance(root);
    }

    private void preOrderTraversal() {
        preOrderTraversal(root);
    }

    private void preOrderTraversal(Node<T> root){
        if (root != null) {
            System.out.print(root.item);
            preOrderTraversal(root.left);
            preOrderTraversal(root.right);
        }
    }

    public void inOrderTraversal() {
        inOrderTraversal(root);
    }

    private void inOrderTraversal(Node<T> root) {
        if (root != null) {
            inOrderTraversal(root.left);
            System.out.println(root.item);
            inOrderTraversal(root.right);
        }

    }

    public void postOrderTraversal() {
        postOrderTraversal(root);
    }

    private void postOrderTraversal(Node<T> root) {
        if (root != null) {
            postOrderTraversal(root.left);
            postOrderTraversal(root.right);
            System.out.println(root.item);
        }
    }

    public void levelOrderTraversal() {
        int height = getTreeHeight(root);

        for (int i = 1; i <= height; ++i) {
            printLevel(root, i);
        }
    }

    private void printLevel(Node<T> root, int level) {
        if (root == null) {
            return;
        }

        if (level == 1) {
            System.out.println(root.item);
        }  else {
            printLevel(root.left, level - 1);
            printLevel(root.right, level - 1);
        }
    }



    public boolean search(T data) {
        return search(data, root) != null;
    }

    private Node<T> search(T data, Node<T> root) {
        Node<T> node = root;

        while (node != null && node.item != null && node.item != data ) {
            if (data.compareTo(node.item) < 0) {
                node = node.left;
            }   else {
                node = node.right;
            }
        }

        return node;
    }

    public T findMinData() {
        return findMinNode(root).item;
    }

    private Node<T> findMinNode(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public T findMaxData() {
        return findMaxNode(root).item;
    }

    private Node<T> findMaxNode(Node<T> node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    public boolean delete(T data) {
        if (this.delete(data, root) == null ) {
            return false;
        }

        --this.nodesCount;
        return true;
    }

    private Node<T> delete(T data, Node<T> root) {
        if (root == null) {
            return root;
        }

        if (data.compareTo(root.item) > 0) {
            root.right = delete(data, root.right);
        } else if (data.compareTo(root.item) < 0) {
            root.left = delete(data, root.left);
        } else {
            if (root.left == null ) {
                root = root.right;
            } else if (root.right == null) {
                root = root.left;
            } else {
                root.item =  this.findMinNode(root.right).item;
                root.right = delete(root.item, root.right);
            }
        }

        if (root != null) {
            reBalance(root);
        }

        return root;
    }

    public void clear() {
        this.root = null;
    }

    public int getTreeHeight() {
        return getTreeHeight(root);
    }

    private int getTreeHeight(Node<T> root) {
        if (root == null) {
            return 0;
        }

        int left = getTreeHeight(root.left);
        int right = getTreeHeight(root.right);

        return Math.max(left,right) + 1;
    }

    private int height(Node<T> node) {
        return node == null ? -1 : node.height;
    }

    private void updateHeight(Node<T> node) {
        node.height = Math.max(height(node.left), height(node.left)) + 1;
    }

    private int getBalance(Node<T> node) {
        return node == null ? 0 : height(node.right) - height(node.left);
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> root = node.left;
        Node<T> leaf = root.right;
        root.right = node;
        node.left = leaf;
        updateHeight(node);
        updateHeight(root);
        return root;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> root = node.right;
        Node<T> leaf = root.left;
        root.left = node;
        node.right = leaf;
        updateHeight(node);
        updateHeight(root);
        return root;
    }


    private Node<T> reBalance(Node<T> node) {
        updateHeight(node);
        int balanceFactor = getBalance(node);
        if (balanceFactor > 1) {
            if (getBalance(node.right) >= 0) {
                node = rotateLeft(node);
            } else  {
                node.right = rotateRight(node.right);
                node = rotateLeft(node);
            }
        }
        if (balanceFactor < -1) {
            if (getBalance(node.left)  <= 0) {
                node = rotateRight(node);
            } else  {
                node.left = rotateLeft(node.left);
                node = rotateRight(node);
            }
        }

        return node;
    }

    public Object[] toSortedArray(){
        Object[] result = new Object[this.nodesCount];
        Node<T> current = this.root;
        int index = 0;
        while (current != null) {
            if (current.left == null) {
                result[index++] = current.item;
                current = current.right;
            } else {
                Node<T> predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                } else {
                    predecessor.right = null;
                    result[index++] = current.item;
                    current = current.right;
                }
            }
        }

        return result;
    }

    public List<T> toSortedList() {
        List<T> list = new ArrayList<>();
        Node<T> current = this.root;
        while (current != null) {
            if (current.left == null) {
                list.add(current.item);
                current = current.right;
            } else {
                Node<T> predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                } else {
                    predecessor.right = null;
                    list.add(current.item);
                    current = current.right;
                }
            }
        }
        return list;
    }


}
