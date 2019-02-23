module.exports = (props = {}, classNames = []) => {
  if (props.className) {
    return classNames.concat(props.className
      .replace(/\s+/, ' ').split(' '));
  } else if (props.classNames) {
    return classNames.concat(props.classNames
      .replace(/\s+/, ' ').split(' '));
  }
  return classNames;
};
