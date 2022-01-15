package com.bell.arithmetic.all;


import java.rmi.dgc.Lease;
import java.util.LinkedList;
import java.util.Queue;


public class P37 {
    public static void main(String[] args) {

    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        if(root != null){
            sb.append(root.val);
            sb.append(",");
        }else{
            return"null";
        }
        sb.append(serialize(root.left));
        sb.append(serialize(root.right));
        return sb.toString();
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        String[] s = data.split(",");
        TreeNode root = null;
        return ded(root, s);
    }
    int index = -1;
    private TreeNode ded(TreeNode node, String[] s){
        if(!s[++index].equals("null")) node = new TreeNode(Integer.parseInt(s[index]));
        else return node;
        node.left = ded(node.left, s);
        node.right = ded(node.right, s);
        return node;
    }


    public static String serialize1(TreeNode root) {
        if(root == null){
            return "[]";
        }
        StringBuilder str = new StringBuilder("[");
        Queue<TreeNode> queue = new LinkedList<TreeNode>(){};
        queue.add(root);
        while(!queue.isEmpty()){
            TreeNode poll = queue.poll();
            if (poll != null) {
                str.append(poll.val).append(",");
                queue.add(poll.left);
                queue.add(poll.right);
            }else{
                str.append("null,");
            }
        }
        str.deleteCharAt(str.length() - 1);
        str.append("]");
        return str.toString();

    }

    // Decodes your encoded data to tree.
    public static TreeNode deserialize1(String data) {
        if(data.equals("[]")){
            return null;
        }
        String[] strs = data.substring(data.length() - 1).split(",");
        TreeNode root = new TreeNode(Integer.parseInt(strs[0]));
        Queue<TreeNode> treeNodes = new LinkedList<TreeNode>() {};
        treeNodes.add(root);
        int i = 1;
        while(!treeNodes.isEmpty()){
            TreeNode poll = treeNodes.poll();
            if(!strs[i].equals("null")) {
                poll.left = new TreeNode(Integer.parseInt(strs[i]));
                treeNodes.add(poll.left);
            }
            i++;
            if(!strs[i].equals("null")){
                poll.right = new TreeNode(Integer.parseInt(strs[i]));
                treeNodes.add(poll.left);
            }
            i++;
        }
        return root;
    }
}
