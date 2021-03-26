#!/bin/bash

#cd to script dir
cd "$(dirname "$0")"

function init() {
  echo "---- Downloading sample profile.yaml file ----"
  chmod +x $0
  wget https://raw.githubusercontent.com/jarviscanada/jarvis_profile_builder/develop/profile.yaml .
  exit 0
}

function check_status() {
  exit_code=$1
  if [ ${exit_code} -eq 0 ]
  then
    echo "Success!!!👍"
    echo ""
  else
    echo "Failed"
    exit 1
  fi
}

function validate_yaml() {
  echo "---- Validating profile.yaml file ----"
  docker pull jrvs/yamale
  docker pull mikefarah/yq:3.3.4
  docker run --rm -v "${PWD}":/workdir jrvs/yamale yamale -s /schema/profile_schema.yaml profile.yaml
  check_status $?
}

function get_profile_name() {
  echo "---- Parsing metadat ----"
  profile_name=$(docker run -it --rm -v "${PWD}":/workdir mikefarah/yq:3.3.4 yq r profile.yaml name  | xargs | tr -d '\r' | sed -e 's/ /_/g')
  profile_prefix=jarvis_profile_${profile_name}
  echo ${profile_name}
  check_status $?
}

function yaml_to_json() {
  echo "---- Coverting profile YAML to JSON ----"
  docker run --rm -v "${PWD}":/workdir mikefarah/yq:3.3.4 yq r -j --prettyPrint profile.yaml > profile.json
  check_status $?
}

function render_md() {
  echo "---- Rendering profile.md ----"
  docker pull jrvs/render_profile_md
  docker run --rm -it -v "${PWD}":/workdir jrvs/render_profile_md  profile.yaml profile.md
  check_status $?
}

function render_pdf() {
  echo "---- Rendering profile.pdf ----"
  template_profile=profile.md
  output_profile_pdf=${profile_prefix}.pdf

  top_bot_margin=1.75cm
  left_right_margin=1.5cm
  font_size=8

  docker run --rm --volume "$(pwd):/data" --user $(id -u):$(id -g) pandoc/latex:2.9.2.1 \
    ${template_profile} -f markdown -t pdf -s \
    --pdf-engine=xelatex -V pagestyle=empty -V fontsize=${font_size}pt -V geometry:"top=${top_bot_margin}, bottom=${top_bot_margin}, left=${left_right_margin}, right=${left_right_margin}" -o ${output_profile_pdf}
  check_status $?
}

function overwrite_readme() {
  if ls ../README.md; then
    echo "---- Moving profile.md to ../README.md ----"
    mv profile.md ../README.md
  fi
}

if [ "$1" = "init" ]; then
  init
fi

validate_yaml
get_profile_name
yaml_to_json
render_md
render_pdf
overwrite_readme

echo "Done!"
exit 0
