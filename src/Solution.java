import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

// 剑指 Offer 59 - I. 滑动窗口的最大值
public class Solution {
    public static void main(String[] args) {
        int[] nums = {1,3,-1,-3,5,3,6,7};
        LinkedList<Integer> ls = new LinkedList<>();

        ls.add(nums[0]);
        ls.add(nums[1]);
        for(int i=2;i<nums.length;i++){
            ls.add(nums[i]);
            int max=Collections.max(ls);
            System.out.println((max));
            ls.removeFirst();
        }
    }

    public int[] maxSlidingWindow(int[] nums, int k) {
        LinkedList<Integer> ls = new LinkedList<>();
        int[] ans = new int[nums.length-k+1];
        for(int i=0;i<k;i++){
            ls.add(nums[i]);
        }
        for(int i=k;i<nums.length;i++){

            int max=Collections.max(ls);
            ans[i-k] = max;
            ls.add(nums[i]);
            ls.removeFirst();
        }
        return ans;
    }
}

