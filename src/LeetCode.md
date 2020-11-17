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

