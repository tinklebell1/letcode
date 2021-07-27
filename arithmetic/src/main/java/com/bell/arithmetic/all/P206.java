package com.bell.arithmetic.all;

public class P206 {
    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
    //1,2,3,4,5,null
    //
    class Solution {
        public ListNode reverseList(ListNode head) {
            ListNode pre = null;
            ListNode curr = head;
            while(curr != null){
                ListNode nextTemp = curr.next;
                curr.next = pre;
                pre = curr;
                curr = nextTemp;
            }
            return pre;
        }
    }
}
