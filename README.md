# Trees

This repository contains implementations of Binary Search Tree (BST) and AVL Tree data structures in Java.
Binary Search Tree (BST)

The Binary Search Tree (BST) is a fundamental data structure that maintains a sorted collection of elements. It supports efficient operations such as insertion, deletion, and search. The BST implementation provided in this repository includes the following functions:

    insert(key, value): Inserts a key-value pair into the BST.
    delete(key): Removes the node with the specified key from the BST.
    search(key): Searches for a node with the specified key in the BST and returns its value if found.
    toSortedArray(): Returns an array containing the keys of the BST in sorted order.
    toSortedList(): Returns a linked list containing the keys of the BST in sorted order.

AVL Tree

The AVL Tree is a self-balancing binary search tree that ensures the tree remains balanced, providing guaranteed O(log n) time complexity for insertion, deletion, and search operations. The AVL Tree implementation provided in this repository includes the same functions as the BST implementation, along with additional functions for AVL tree-specific operations:

    rotateLeft(node): Performs a left rotation on the specified node.
    rotateRight(node): Performs a right rotation on the specified node.
    getHeight(node): Returns the height of the specified node.
    getBalanceFactor(node): Returns the balance factor of the specified node.
    reBalanced(node): Checks if the subtree rooted at the specified node is balanced.

Usage

To use the BST and AVL Tree implementations in your Java project, follow these steps:

    Clone the Trees repository: git clone <repository-url>
    Include the necessary Java files in your project.
    Create an instance of BST or AVLTree class.
    Use the available functions to perform operations on the tree.
