package sample;

public class Merge {
    private static void merge(Comparable[] arr, Comparable[] aux, int low, int mid, int high) {
        for (int i = low; i <= high; i++) {
            aux[i] = arr[i];
        }
        int i = low;
        int j = mid+1;
        for (int k = low; k <= high; k++) {
            if(i>mid){
                arr[k]=aux[j++];
            }else if(j>high){
                arr[k]=aux[i++];
            }else if(less(aux[j],aux[i])){
                arr[k]=aux[j++];
            }else{
                arr[k]=aux[i++];
            }
        }
    }

    private static void sort(Comparable[] arr, Comparable[] aux, int low, int high) {
        if (high <= low) {
            return;
        }
        int mid = low + (high - low) / 2;
        sort(arr, aux, low, mid);
        sort(arr, aux, mid + 1, high);
        merge(arr, aux, low, mid, high);
    }

    public static void sort(Comparable[] arr) {
        Comparable[] aux = new Comparable[arr.length];
        sort(arr, aux, 0, arr.length-1);
    }

    private static boolean less(Comparable v, Comparable w) {
        return (v.compareTo(w) < 0);
    }
}
