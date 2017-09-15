/*
 * MergeSort.h
 *
 *  Created on: 29 Aug 2014
 *      Author: sgerasimou
 */

#ifndef MERGESORT_H_
#define MERGESORT_H_

#include <vector>

using namespace std;

struct Configuration{
	  double readings;
	  double powerConsumption;
	  double contribution;
};

vector<double> mergeVector(const vector<double>& left, const vector<double>& right);

vector<double> mergeSortVector(vector<double>& vec);

vector<Configuration> mergeVectorConfiguration(const vector<Configuration>& left, const vector<Configuration>& right);

vector<Configuration> mergeSortVectorConfiguration(vector<Configuration>& vec);



#endif /* MERGESORT_H_ */
