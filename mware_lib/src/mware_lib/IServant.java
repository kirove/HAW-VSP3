package mware_lib;

/**
 * Created by Cenan on 11.12.13.
 */
public interface IServant {


    Skeleton getSkeleton(IServant servant);

    /**
     * only for debugging reasons
     */
    String getSkeletonType();
}
