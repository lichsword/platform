echo step1...passed
git add .
echo "step1: Add...(20%)"
git commit -m \"$1\"
echo "step2: Commit...(40%)"
git pull --rebase
echo "step3: Pull...(60%)"
git push
echo "step4: Push...(80%)"
git pull --rebase
echo "step5: Pull...(100%)"
