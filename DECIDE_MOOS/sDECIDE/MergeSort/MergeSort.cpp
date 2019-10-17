/*
 * MergeSort.cpp
 *
 *  Created on: 29 Aug 2014
 *      Author: sgerasimou
 */

#include "MergeSort.h"
using namespace std;

//! \brief Performs a recursive merge sort on the given vector
//! \param vec The vector to be sorted using the merge sort
//! \return The sorted resultant vector after merge sort is
//! complete.
vector<double> mergeSortVector(vector<double>& vec)
{
    // Termination condition: List is completely sorted if it
    // only contains a single element.
    if(vec.size() == 1)
    {
        return vec;
    }

    // Determine the location of the middle element in the vector
    std::vector<double>::iterator middle = vec.begin() + (vec.size() / 2);

    vector<double> left(vec.begin(), middle);
    vector<double> right(middle, vec.end());

    // Perform a merge sort on the two smaller vectors
    left = mergeSortVector(left);
    right = mergeSortVector(right);

    return mergeVector(left, right);
}

//! \brief Merges two sorted vectors into one sorted vector
//! \param left A sorted vector of integers
//! \param right A sorted vector of integers
//! \return A sorted vector that is the result of merging two sorted
//! vectors.
vector<double> mergeVector(const vector<double>& left, const vector<double>& right)
{
    // Fill the resultant vector with sorted results from both vectors
    vector<double> result;
    unsigned left_it = 0, right_it = 0;

    while(left_it < left.size() && right_it < right.size())
    {
        // If the left value is smaller than the right it goes next
        // into the resultant vector
        if(left[left_it] < right[right_it])
        {
            result.push_back(left[left_it]);
            left_it++;
        }
        else
        {
            result.push_back(right[right_it]);
            right_it++;
        }
    }

    // Push the remaining data from both vectors onto the resultant
    while(left_it < left.size())
    {
        result.push_back(left[left_it]);
        left_it++;
    }

    while(right_it < right.size())
    {
        result.push_back(right[right_it]);
        right_it++;
    }

    return result;
}



//! \brief Performs a recursive merge sort on the given vector
//! \param vec The vector to be sorted using the merge sort
//! \return The sorted resultant vector after merge sort is
//! complete.
vector<Configuration> mergeSortVectorConfiguration(vector<Configuration>& vec)
{
    // Termination condition: List is completely sorted if it
    // only contains a single element.
    if(vec.size() == 1)
    {
        return vec;
    }

    // Determine the location of the middle element in the vector
    std::vector<Configuration>::iterator middle = vec.begin() + (vec.size() / 2);

    vector<Configuration> left(vec.begin(), middle);
    vector<Configuration> right(middle, vec.end());

    // Perform a merge sort on the two smaller vectors
    left = mergeSortVectorConfiguration(left);
    right = mergeSortVectorConfiguration(right);

    return mergeVectorConfiguration(left, right);
}

//! \brief Merges two sorted vectors into one sorted vector
//! \param left A sorted vector of integers
//! \param right A sorted vector of integers
//! \return A sorted vector that is the result of merging two sorted
//! vectors.
vector<Configuration> mergeVectorConfiguration(const vector<Configuration>& left, const vector<Configuration>& right)
{
    // Fill the resultant vector with sorted results from both vectors
    vector<Configuration> result;
    unsigned left_it = 0, right_it = 0;

    while(left_it < left.size() && right_it < right.size())
    {
        // If the left value is smaller than the right it goes next
        // into the resultant vector
        if(left[left_it].contribution < right[right_it].contribution)
        {
            result.push_back(left[left_it]);
            left_it++;
        }
        else
        {
            result.push_back(right[right_it]);
            right_it++;
        }
    }

    // Push the remaining data from both vectors onto the resultant
    while(left_it < left.size())
    {
        result.push_back(left[left_it]);
        left_it++;
    }

    while(right_it < right.size())
    {
        result.push_back(right[right_it]);
        right_it++;
    }

    return result;
}




