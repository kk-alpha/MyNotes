[TOC]

## 树

### [100. 相同的树](https://leetcode-cn.com/problems/same-tree/)

+ ```java
  class Solution {
      public boolean isSameTree(TreeNode p, TreeNode q) {
          if(p!=null&&q!=null&&p.val==q.val){
              return isSameTree(p.left,q.left)&&isSameTree(p.right,q.right);
          }
          else if(p==null&&q==null) return true;
          else return false;
      }
  }
  ```

+ 

### [101. 对称二叉树](https://leetcode-cn.com/problems/symmetric-tree/)

+ ```java
  class Solution {
      public boolean isSymmetric(TreeNode root) {
          return root == null ? true : helper(root.left, root.right);
      } 
      public boolean helper(TreeNode left, TreeNode right){
          if(left!=null && right!=null && left.val == right.val)
              return helper(left.left, right.right) && helper(left.right, right.left);
          else if(left == null && right == null) return true;
          else return false;
      }
  }
  ```

### [104. 二叉树的最大深度](https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/)

+ ```java
  class Solution {
      int max = 0;
      public int maxDepth(TreeNode root) {
          if(root != null){
              return Math.max(maxDepth(root.left),maxDepth(root.right))+1;
          }
          return max;
      }
  }
  ```

+ 

### [111. 二叉树的最小深度](https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/)

+ ```java
  class Solution {
      public int minDepth(TreeNode root) {
      if(root==null) return 0;
      else if(root.left==null&&root.right==null) return 1;
      int a = minDepth(root.left);
      int b = minDepth(root.right);
      if(a==0||b==0)return a+b+1;
      else return Math.min(a,b)+1;
      }
  }
  ```

### [107. 二叉树的层次遍历 II](https://leetcode-cn.com/problems/binary-tree-level-order-traversal-ii/)

+ ```java
  class Solution {
      LinkedList<List<Integer>> list = new LinkedList<>();
      LinkedList<TreeNode> queue = new LinkedList<>();
      
      TreeNode node;
      public List<List<Integer>> levelOrderBottom(TreeNode root) {
          if(root==null) return list; 
          queue.add(root);
          while(!queue.isEmpty()){
              List<Integer> array = new ArrayList<>();
              int count = queue.size();
              for(int i = 0; i<count; i++){
                  node = queue.removeFirst();
                  if(node.left!=null)
                      queue.addLast(node.left);
                  if(node.right!=null)
                      queue.addLast(node.right);
                  array.add(node.val);
              }
              list.addFirst(array);
          }
          return list;
      } 
  }
  ```

### [108. 将有序数组转换为二叉搜索树](https://leetcode-cn.com/problems/convert-sorted-array-to-binary-search-tree/)

+ ```java
  class Solution {
      public TreeNode sortedArrayToBST(int[] nums) {
          return helper(nums,0,nums.length-1);
      }
      public TreeNode helper(int[] nums, int left, int right){
          if(left > right){
              return null;
          }
          int mid = (left + right) / 2;
          TreeNode root = new TreeNode(nums[mid]);
          root.left = helper(nums,left,mid-1);
          root.right = helper(nums,mid+1,right);
          return root;
      }
  }
  ```


### [110. 平衡二叉树](https://leetcode-cn.com/problems/balanced-binary-tree/)

+ ```java
  class Solution {
    
      public boolean isBalanced(TreeNode root) {
          if(root == null) return true;
          return isBST(root).isBSTree;
      }
  
      public Node isBST(TreeNode root){
          if(root == null) return new Node(0,true);
          Node left = isBST(root.left);
          Node right = isBST(root.right);
          if(left.isBSTree==false||right.isBSTree==false) return new Node(0,false);
          if(Math.abs(left.height-right.height)>1) return new Node (0,false);
          return new Node(Math.max(left.height,right.height) + 1,true);
      }
  
      class Node{
          int height;
          boolean isBSTree;
          Node(int height, boolean isBSTree){
              this.height = height;
              this.isBSTree = isBSTree;
          }
      }
  }
  ```

### [235. 二叉搜索树的最近公共祖先](https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-search-tree/)

+ ```java
  class Solution {
      TreeNode ans;
      public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
          helper(root,p,q);
          return ans;
      }
      public void helper(TreeNode root, TreeNode p, TreeNode q){
          if((p.val-root.val)*(q.val-root.val)<=0){
              ans = root;
          }
          else if(p.val-root.val>0) helper(root.right,p,q);
          else helper(root.left,p,q);
      }
  }
  ```


### [257. 二叉树的所有路径](https://leetcode-cn.com/problems/binary-tree-paths/)

+ ```java
  //回溯实现
  class Solution {
      List<String> ans = new ArrayList<>();
      public List<String> binaryTreePaths(TreeNode root) {
          if(root == null) return ans;
          helper(new StringBuilder(),root);
          return ans;
      }
      public void helper(StringBuilder s, TreeNode root){
          s.append("->");
          s.append(root.val);
          int len = 2 + String.valueOf(root.val).length();
          if(root.left == null && root.right == null) {
              ans.add(s.substring(2,s.length()));
              s.delete(s.length()-len,s.length());
              return;    
          }
          if(root.left != null){
              helper(s, root.left);
          }
          if(root.right != null){
              helper(s, root.right);
          }
          s.delete(s.length()-len,s.length());
      }
  }
  ```

  

### [501. 二叉搜索树中的众数](https://leetcode-cn.com/problems/find-mode-in-binary-search-tree/)

+ ```java
  public class Solution {
      List<Integer> list = new ArrayList<>();
      int preValue = 0;
      int curCount = 0;
      int maxCount = 0;
      public int[] findMode(TreeNode root) {
          helper(root);
          int[] ans = new int[list.size()];
          for(int i=0;i<list.size();i++){
              ans[i] = list.get(i);
          }
          return ans;
      }
      public void helper(TreeNode root){
          if(root!=null){
              helper(root.left);
              if(preValue != root.val) curCount = 1;
              else curCount++;
              if(curCount == maxCount) list.add(root.val);
              else if(curCount > maxCount) {
                  list.clear();
                  list.add(root.val);
                  maxCount = curCount;
              }
              preValue = root.val;
              helper(root.right);
          }
      }
  }
  ```

+ 

### [530. 二叉搜索树的最小绝对差](https://leetcode-cn.com/problems/minimum-absolute-difference-in-bst/)

+ ```java
  class Solution {
      int min = Integer.MAX_VALUE, pre = -1, diff;
      public int getMinimumDifference(TreeNode root) {
          if(root==null)return 0;
          getMinimumDifference(root.left);
          if(pre == -1) pre = root.val;
          else{
              diff = root.val-pre;
              min = diff < min ? diff : min;
              pre = root.val;
          }
          getMinimumDifference(root.right);
          return min;
      }
  }
  ```

### [543. 二叉树的直径](https://leetcode-cn.com/problems/diameter-of-binary-tree/)

+ ```java
  class Solution {
      //根结点到左子树的最长路径+到右子树的最长路径
      int max=0;
      public int diameterOfBinaryTree(TreeNode root) {
          if(root==null) return 0;
          helper(root);
          return max;
      }
      public int helper(TreeNode root){
          if(root.left==null&&root.right==null) return 0;
          int left = root.left==null?0:helper(root.left)+1;
          int right = root.right==null?0:helper(root.right)+1;
          max = Math.max(left+right,max);
          return Math.max(left,right);
      }
  }
  ```

### [559. N叉树的最大深度](https://leetcode-cn.com/problems/maximum-depth-of-n-ary-tree/)

+ ```java
  class Solution {
      int max = 0;
      public int maxDepth(Node root) {
          if(root == null) return 0;
          helper(root);
          return max+1;
      }
      public int helper(Node root){
          if(root.children.size() == 0) return 0;
          int temp_max=0;
          for(Node node : root.children){
              int temp = node==null?0:helper(node)+1;
              temp_max = Math.max(temp_max,temp);
          }
          max = Math.max(max,temp_max);
          return temp_max;
      }
  }
  ```

### [563. 二叉树的坡度](https://leetcode-cn.com/problems/binary-tree-tilt/)

+ ```java
  // 1.用Node存返回值
  class Solution {
      int res = 0;
      //返回子节点的和->得到父节点坡度，返回子节点的差->得到本节点的坡度
      public int findTilt(TreeNode root) {
          if(root == null) return 0;
          helper(root);
          return res;
      }
      public Node helper(TreeNode root){
          if(root == null) return new Node(0,0);
          Node left = helper(root.left);
          Node right = helper(root.right);
          int gradient = Math.abs(left.sum-right.sum);
          res += gradient;
          return new Node(left.sum+right.sum+root.val,gradient);
      }
  
      class Node {
          int sum;
          int gradient;
          public Node(int sum, int gradient){
              this.sum = sum;
              this.gradient = gradient;
          }
      }
  }
  
  //2.发现Node第二个参数没用，优化
  class Solution {
      int res = 0;
      //返回子节点的和->得到父节点坡度，返回子节点的差->得到本节点的坡度
      public int findTilt(TreeNode root) {
          helper(root);
          return res;
      }
      public int helper(TreeNode root){
          if(root == null) return 0;
          int left = helper(root.left);
          int right = helper(root.right);
          int gradient = Math.abs(left-right);
          res += gradient;
          return left+right+root.val;
      }
  }
  ```


### [572. 另一个树的子树](https://leetcode-cn.com/problems/subtree-of-another-tree/)

+ ```java
  class Solution {
      public boolean isSubtree(TreeNode s, TreeNode t) {
          if(s == null && t == null) return true;
          else if(s == null || t == null) return false;
          return isSameTree(t,s)||isSubtree(s.left,t)||isSubtree(s.right,t);
          // LinkedList<TreeNode> list = new LinkedList<>();
          // list.add(s);
          // boolean flag = true;
          // TreeNode node = new TreeNode();
          // while(!list.isEmpty()&&flag==true){
          //     int size = list.size();
          //     for(int i = 0; i < size; i++) {
          //         node = list.remove();
          //         if(node.val == t.val) {
          //             flag = false;
          //             break;
          //         }
          //         if(node.left != null) list.offer(node.left);
          //         if(node.right != null) list.offer(node.right);
          //     }
          // }
          // if(flag==true) return false;
          // return isSameTree(node,t);
      }
      public boolean isSameTree(TreeNode s, TreeNode t){
          if(s==null&&t==null) return true;
          if(s==null||t==null) return false;
          if(s.val != t.val) return false;
          return isSameTree(s.left,t.left)&&isSameTree(s.right,t.right);
      
      }
  }
  ```


### [589. N叉树的前序遍历](https://leetcode-cn.com/problems/n-ary-tree-preorder-traversal/)

+ ```java
  //递归
  class Solution {
      List<Integer> list = new ArrayList<>();
  
      public List<Integer> preorder(Node root) {
          if(root==null) return list;
          list.add(root.val);
          for(Node node : root.children){
              preorder(node);
          }
          return list;
      }
  }
  
  //迭代  TODO
  
  ```

### [590. N叉树的后序遍历](https://leetcode-cn.com/problems/n-ary-tree-postorder-traversal/)

+ ```java
  //迭代
  class Solution {
      LinkedList<Node> list1 = new LinkedList<>();
      LinkedList<Integer> list2 = new LinkedList<>();
      public List<Integer> postorder(Node root) {
          if(root==null) return list2;
          list1.push(root);
          while(!list1.isEmpty()){
              Node temp = list1.pop();
              for(Node node : temp.children){
                  list1.push(node);
              }
              list2.push(temp.val);
          }
          return list2;
      }
  }
  ```


### [617. 合并二叉树](https://leetcode-cn.com/problems/merge-two-binary-trees/)

+ ```java
  // 不修改原来的树
  class Solution {
      public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
          if (t1 == null && t2 == null) return null;
          TreeNode res = new TreeNode(0);
          TreeNode left1 = null, left2 = null, right1 = null, right2 = null;
          int sum = 0;
          if(t1 != null) {
              sum += t1.val;
              left1 = t1.left;
              right1 = t1.right;
          }
          if(t2 != null) {
              sum += t2.val;
              left2 = t2.left;
              right2 = t2.right;
          }
          res.val = sum;
          res.left = mergeTrees(left1, left2);
          res.right = mergeTrees(right1, right2);
          return res;
      }
  }
  // 在原树上修改
  class Solution {
      
      public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
          if (t1 == null) return t2;
          if (t2 == null) return t1;
          
          t1.val += t2.val;
          t1.left = mergeTrees(t1.left, t2.left);
          t1.right = mergeTrees(t1.right, t2.right);
          return t1;
      }
  }
  ```

### [637. 二叉树的层平均值](https://leetcode-cn.com/problems/average-of-levels-in-binary-tree/)

+ ```java
  //层次遍历
  class Solution {
      public List<Double> averageOfLevels(TreeNode root) {
          LinkedList<TreeNode> list = new LinkedList<>();
          List<Double> res = new ArrayList<>();
          list.add(root);
          while (!list.isEmpty()) {
              double sum = 0;
              int len = list.size();
              for (int i = 0; i < len; i++){
                  TreeNode node = list.remove();
                  sum += node.val;
                  if (node.left != null) list.add(node.left);
                  if (node.right != null) list.add(node.right);
              }
              res.add(sum / len);
          }
          return res;
      }
  }
  ```


### [653. 两数之和 IV - 输入 BST](https://leetcode-cn.com/problems/two-sum-iv-input-is-a-bst/)

+ ```java
  // 搜索二叉树遍历
  class Solution {
      public boolean findTarget(TreeNode root, int k) {
          List<Integer> list = new ArrayList<>();
          helper(root, list);
          int left = 0, right = list.size()-1;
          while (left < right){
              if (k - list.get(right) < list.get(left)) right--;
              else if (k - list.get(right) > list.get(left)) left++;
              else if (k - list.get(right) == list.get(left)) return true;
          }
          return false;
      }
      public void helper(TreeNode root, List<Integer> list) {
          if (root == null) return;
          helper(root.left, list);
          list.add(root.val);
          helper(root.right, list);
      }
  }
  ```

+ 



## 链表

### [24. 两两交换链表中的节点](https://leetcode-cn.com/problems/swap-nodes-in-pairs/)

+ ```java
  class Solution {
      public ListNode swapPairs(ListNode head) {
          if(head==null||head.next==null) return head;
          ListNode next = head.next;
          head.next = swapPairs(next.next);
          next.next = head;
          return next;
      }
  }
  ```


### [147. 对链表进行插入排序](https://leetcode-cn.com/problems/insertion-sort-list/)

+ ```java
  class Solution {
      public ListNode insertionSortList(ListNode head) {
          ListNode first = new ListNode(0);
          ListNode pre;
          first.next = head;
          
          while(head != null && head.next != null){
              if(head.next.val >= head.val){
                  head = head.next;
                  continue;
              }
              
              pre = first;
              while(pre.next.val < head.next.val){ 
                  pre = pre.next;
              }
              ListNode temp = head.next;
              head.next = temp.next;
              temp.next = pre.next;
              pre.next = temp;
          }
          return first.next;
      }
  }
  ```

### [剑指 Offer 24. 反转链表](https://leetcode-cn.com/problems/fan-zhuan-lian-biao-lcof/)

+ ```java
  //迭代
  class Solution {
      public ListNode reverseList(ListNode head) {
          ListNode pre =null, cur = head, next = null;
          while(cur != null){
              next = cur.next;
              cur.next = pre;
              pre = cur;
              cur = next;
          }
          return pre;
      }
  }
  ```


### [面试题 02.07. 链表相交](https://leetcode-cn.com/problems/intersection-of-two-linked-lists-lcci/)

+ ```java
  public class Solution {
      public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
          ListNode tempA,tempB;
          tempA = headA;
          tempB = headB;
          while(headA!=headB){
              if(headA==null){
                  headA = tempB;
              }
              else headA = headA.next;
              if(headB == null){
                  headB = tempA;
              }
              else headB = headB.next;
          }
          return headB; 
      }
  }
  ```

+ 

## 排序

#### [242. 有效的字母异位词](https://leetcode-cn.com/problems/valid-anagram/)

+ ```java
  class Solution {
      public boolean isAnagram(String s, String t) {
          if(s.length()!=t.length()) return false;
          char[] str1 = s.toCharArray();
          char[] str2 = t.toCharArray();
          Arrays.sort(str1);
          Arrays.sort(str2);
          for(int i=0;i<str1.length;i++){
              if(str1[i]!=str2[i]) return false;
          }
          return true;
      }
  }
  ```

+ 