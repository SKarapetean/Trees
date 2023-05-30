package BinarySearchTree;

import java.io.*;
import java.util.*;

public class BST<T> implements Serializable{
    private static final long serialVersionUID = 1234567891234567L;
    private Comparator<T> cmp;
    private Node<T> root;

    public BST() {}

    public BST(Comparator<T> cmp) {
        this.cmp = cmp;
    }

    public BST(T root){
        this.root = new Node<>(root);
    }
    public BST(Comparator<T> cmp, T root) {
        this.cmp = cmp;
        this.root = new Node<>(root);
    }

    private static class Node<T> implements Serializable{
        private static final long serialVersionUID = 852963741526312652L;
        T data;
        Node<T> left;
        Node<T> right;

        Node(T data){
            this.data = data;
            this.left = null;
            this.right = null;
        }

        public String toString() {
            return data.toString();
        }
    }

    public void insert(T data) {
       root = insert(data, root);
    }

    private Node<T> insert(T data, Node<T> root) {
        if (root == null) {
            return new Node<>(data);
        }

        if (cmp != null) {
            if (cmp.compare(root.data, data) > 0) {
                root.right = insert(data, root.right);
            } else if (cmp.compare(root.data, data) < 0) {
                root.left = insert(data, root.left);
            }
        } else {
            if (((Comparable<T>)data).compareTo(root.data) > 0){
                root.right = insert(data, root.right);
            } else if (((Comparable<T>)data).compareTo(root.data) < 0) {
                root.left = insert(data, root.left);
            }
        }

        return root;
    }

    public boolean search(T data) {
        boolean bool;
        if (cmp != null) {
            bool = searchWithComparator(data, root) == null ? false : true;
        } else {
            bool = searchWithComparable((Comparable)data, root) == null ? false : true;
        }
        return bool;
    }

    private Node<T> searchWithComparator(T data, Node<T> root) {
        if (root == null || cmp.compare(data, root.data) == 0) {
            return root;
        }

        if (cmp.compare(data, root.data) > 0) {
            return searchWithComparator(data, root.right);
        } else {
            return searchWithComparator(data, root.left);
        }
    }

    private Node<T> searchWithComparable(Comparable<T> data, Node<T> root) {
        if (root == null || data.compareTo(root.data) == 0) {
            return root;
        }

        if (data.compareTo(root.data) > 0) {
            return searchWithComparable(data, root.right);
        } else {
            return searchWithComparable(data, root.left);
        }
    }

    public boolean delete(T data) {
        boolean bool;
        if (cmp != null) {
            bool = deleteWithComparator(data, root) != null;
        } else {
            bool = deleteWithComparable((Comparable<T>) data, root) != null;
        }

        return bool;
    }

    private Node<T> deleteWithComparator(T data, Node<T> root) {
        if (root == null) {
            return null;
        }

        if (cmp.compare(data, root.data) > 0) {
            root.right = deleteWithComparator(data, root.right);
            return root.right;
        } else if (cmp.compare(data, root.data) < 0) {
            root.left = deleteWithComparator(data, root.left);
            return root.left;
        } else {
            if (root.right == null) {
                return root.left;
            } else if (root.left == null) {
                return root.right;
            } else {
                root.data = findMinData(root.right);
                root.right = deleteWithComparator(root.data, root.right);
                return root;
            }
        }

    }

    private Node<T> deleteWithComparable(Comparable<T> data, Node<T> root) {
        if (root == null) {
            return null;
        }

        if (data.compareTo(root.data) > 0) {
            root.right = deleteWithComparable(data, root.right);
            return root.right;
        } else if (data.compareTo(root.data) < 0) {
            root.left = deleteWithComparable(data, root.left);
            return root.left;
        } else {
            if (root.right == null) {
                return root.left;
            } else if (root.left == null) {
                return root.right;
            } else {
                root.data = findMinData(root.right);
                root.right = deleteWithComparable((Comparable<T>) root.data, root.right);
                return root;
            }
        }

    }

    public T findMinData() {
        return findMinData(root);
    }
    private T findMinData(Node<T> root) {
        while (root.left != null) {
            root = root.left;
        }

        return root.data;
    }

    public T findMaxData() {
        return findMaxData(root);
    }

    private T findMaxData(Node<T> root) {
        while (root.right != null) {
            root = root.right;
        }
        return root.data;
    }

    public void preorderTraversal() {
        preorderTraversal(root);
    }
    private void preorderTraversal(Node<T> root) {
        if (root == null) {
            return;
        }
        System.out.println(root.data);
        preorderTraversal(root.left);
        preorderTraversal(root.right);
    }

    public void inorderTraversal() {
        inorderTraversal(root);
    }

    private void inorderTraversal(Node<T> root) {
        if (root == null) {
            return;
        }

        inorderTraversal(root.left);
        System.out.println(root.data);
        inorderTraversal(root.right);
    }

    public void postorderTraversal(){
        postorderTraversal(root);
    }

    private void postorderTraversal(Node<T> root) {
        if (root == null) {
            return;
        }

        postorderTraversal(root.left);
        postorderTraversal(root.right);
        System.out.println(root.data);
    }

    public void levelOrderTraversal() {
        int height = getHeight(root);

        for (int i = 1; i <= height; ++i) {
            printLevel(root, i);
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
            System.out.println(root.data);
        } else if (level > 1) {
            printLevel(root.left, level - 1);
            printLevel(root.right, level - 1);
        }
    }

    public T getSuccessorData(T data) {
        return getSuccessorData(root, data);
    }

    private T getSuccessorData(Node<T> root, T data) {
        Node<T> current;
        if (cmp != null) {
            current = searchWithComparator(data, root);
            if (current == null) {
                return null;
            }
            if (current.right != null) {
                return findMinData(current.right);
            } else {
                Node<T> successor = null;
                Node<T> ancestor = root;
                while (ancestor != current) {
                    if (cmp.compare(current.data,ancestor.data) < 0) {
                        successor = ancestor;
                        ancestor = ancestor.left;
                    } else
                        ancestor = ancestor.right;
                }
                return successor.data;
            }
        } else {
            current = searchWithComparable((Comparable<T>) data, root);
            if (current == null) {
                return null;
            }
            if (current.right != null) {
                return findMinData(current.right);
            } else {
                Node<T> successor = null;
                Node<T> ancestor = root;
                while (ancestor != current) {
                    if (((Comparable<T>) data).compareTo(ancestor.data) < 0) {
                        successor = ancestor;
                        ancestor = ancestor.left;
                    } else
                        ancestor = ancestor.right;
                }
                return successor.data;
            }
        }

    }

    public T getPredecessorData(T data) {
        return getPredecessorData(root, data);
    }

    private T getPredecessorData(Node<T> root, T data) {
        Node<T> current;
        if (cmp != null) {
            current = searchWithComparator(data, root);
            if (current == null) {
                return null;
            }
            if (current.left != null) {
                return findMaxData(current.left);
            } else {
                Node<T> predecessor = null;
                Node<T> ancestor = root;
                while (ancestor != current) {
                    if (cmp.compare(current.data,ancestor.data) > 0) {
                        predecessor = ancestor;
                        ancestor = ancestor.right;
                    } else
                        ancestor = ancestor.left;
                }
                return predecessor.data;
            }
        } else {
            current = searchWithComparable((Comparable<T>) data, root);
            if (current == null) {
                return null;
            }
            if (current.left != null) {
                return findMaxData(current.left);
            } else {
                Node<T> predecessor = null;
                Node<T> ancestor = root;
                while (ancestor != current) {
                    if (((Comparable<T>) data).compareTo(ancestor.data) > 0) {
                        predecessor = ancestor;
                        ancestor = ancestor.right;
                    } else
                        ancestor = ancestor.left;
                }
                return predecessor.data;
            }
        }

    }

    public void clear() {
        root = null;
    }

    public Object[] toArray() {
        List<Object> nodeList = new ArrayList<>();
        if (root == null)
            return new Object[0];

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            nodeList.add(current.data);

            if (current.left != null)
                queue.offer(current.left);
            if (current.right != null)
                queue.offer(current.right);
        }

        Object[] result = new Object[nodeList.size()];
        for (int i = 0; i < nodeList.size(); i++) {
            result[i] = nodeList.get(i);
        }
        return result;
    }

    public Object[] toSortedArray() {
        List<Object> list = new ArrayList<>();
        toSortedList(list, root);
        Object[] result = new Object[list.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = list.get(i);
        }

        return result;
    }

    public void toSortedList(List<Object> list, Node<T> root) {
        if (root == null) {
            return;
        }

        toSortedList(list, root.left);
        list.add(root.data);
        toSortedList(list, root.right);
    }

    public void writeToFile(String filePath) {
        try(FileOutputStream fis = new FileOutputStream(filePath); ObjectOutputStream ois = new ObjectOutputStream(fis);) {
            ois.writeObject(this);
            this.clear();

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private Node<T> getRoot() {
        return root;
    }

    public void readFromFile(String filePath) {
        try(FileInputStream fis = new FileInputStream(filePath); ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.root = ((BST<T>)ois.readObject()).getRoot();
        } catch(IOException e) {
            System.err.println(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
