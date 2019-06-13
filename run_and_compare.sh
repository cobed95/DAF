./daf_1min -d ./data/human -q ./data/human_40n -a ./data/human_40n.dag -n 100 > result1
./daf_1min -d ./data/human -q ./data/human_40n -n 100 > result2
python sort_result.py result1 result2
