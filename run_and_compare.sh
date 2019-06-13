echo "Running with custom DAG construction result..."
./daf_1min -d ./data/human -q ./data/human_40n -a ./data/human_40n.dag -n 100 > result1
echo "Done"
echo "Running with default DAG construction result..."
./daf_1min -d ./data/human -q ./data/human_40n -a ./data/default.dag -n 100 > result2
echo "Done"
python sort_result.py result1 result2
