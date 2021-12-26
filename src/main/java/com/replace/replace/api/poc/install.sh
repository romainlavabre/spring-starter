#!/bin/bash

BASE_DIR="$1"
PACKAGE_PARSER=${BASE_DIR/"$2/src/main/java/com/"/""}
PACKAGES=""

IFS='/' read -ra ARRAY <<<"$PACKAGE_PARSER"
I=0

for PART in "${ARRAY[@]}"; do
    if [ "$I" == "0" ]; then
        PACKAGES="$PART"
    fi

    if [ "$I" == "1" ]; then
        PACKAGES="${PACKAGES}.${PART}"
    fi

    I=$((I + 1))
done

CLASSES=(
    "$1/kernel/util/Formatter.java"
    "$1/kernel/util/TypeResolver.java"
    "$1/kernel/trigger/TriggerHandler.java"
    "$1/kernel/setter/SetterHandler.java"
    "$1/kernel/router/Resolver.java"
    "$1/kernel/router/RouteHandler.java"
    "$1/kernel/exception/InvalidSetterParameterType.java"
    "$1/kernel/exception/MultipleSetterFoundException.java"
    "$1/kernel/exception/NoRouteMatchException.java"
    "$1/kernel/exception/SetterNotFoundException.java"
    "$1/kernel/exception/ToManySetterParameterException.java"
    "$1/kernel/exception/UnmanagedTriggerMissingExecutorException.java"
    "$1/kernel/entry/AbstractUseCase.java"
    "$1/kernel/entry/Controller.java"
    "$1/kernel/entry/Create.java"
    "$1/kernel/entry/CreateEntry.java"
    "$1/kernel/entry/Delete.java"
    "$1/kernel/entry/DeleteEntry.java"
    "$1/kernel/entry/Trigger.java"
    "$1/kernel/entry/Update.java"
    "$1/kernel/entry/UpdateEntry.java"
    "$1/kernel/entity/EntityHandler.java"
    "$1/api/CustomConstraint.java"
    "$1/api/ResourceProvider.java"
    "$1/api/UnmanagedTrigger.java"
    "$1/annotation/Constraint.java"
    "$1/annotation/CreateTrigger.java"
    "$1/annotation/DefaultCreate.java"
    "$1/annotation/DefaultDelete.java"
    "$1/annotation/DefaultUnmanagedExecutor.java"
    "$1/annotation/DefaultUpdate.java"
    "$1/annotation/Delete.java"
    "$1/annotation/DeleteTrigger.java"
    "$1/annotation/EntryPoint.java"
    "$1/annotation/GetAll.java"
    "$1/annotation/GetAllBy.java"
    "$1/annotation/GetOne.java"
    "$1/annotation/GetOneBy.java"
    "$1/annotation/Patch.java"
    "$1/annotation/PocEnabled.java"
    "$1/annotation/Post.java"
    "$1/annotation/Put.java"
    "$1/annotation/RequestParameter.java"
    "$1/annotation/Setter.java"
    "$1/annotation/Trigger.java"
    "$1/annotation/UnmanagedTrigger.java"
    "$1/annotation/UpdateTrigger.java"
)

for CLASS in "${CLASSES[@]}"; do
    sed -i "s|replace.replace|$PACKAGES|" "$CLASS"
done

DIRECTORY="$2/src/main/java/com/${PACKAGES//.//}/configuration/poc"

if [ ! -d "$DIRECTORY" ]; then
    mkdir -p "$DIRECTORY"
fi

if [ -f "$DIRECTORY/Subject.java" ]; then
    read -p "File $DIRECTORY/Subject.java, Overwrite ? [Y/n] " -r OVERWRITE

    if [ "$OVERWRITE" == "Y" ] || [ "$OVERWRITE" == "y" ]; then
        mv "$1/Subject.java" "$DIRECTORY/Subject.java"
    fi

else
    mv "$1/Subject.java" "$DIRECTORY/Subject.java"
fi

if [ -f "$DIRECTORY/TriggerIdentifier.java" ]; then
    read -p "File $DIRECTORY/TriggerIdentifier.java, Overwrite ? [Y/n] " -r OVERWRITE

    if [ "$OVERWRITE" == "Y" ] || [ "$OVERWRITE" == "y" ]; then
        mv "$1/TriggerIdentifier.java" "$DIRECTORY/TriggerIdentifier.java"
    fi

else
    mv "$1/TriggerIdentifier.java" "$DIRECTORY/TriggerIdentifier.java"
fi

sed -i "s|com.replace.replace.api.poc|com.${PACKAGES}.configuration.poc|" "$DIRECTORY/Subject.java"
sed -i "s|com.replace.replace.api.poc|com.${PACKAGES}.configuration.poc|" "$DIRECTORY/TriggerIdentifier.java"
sed -i "s|com.${PACKAGES}.api.poc.TriggerIdentifier|com.${PACKAGES}.configuration.poc.TriggerIdentifier|" "$1/kernel/router/Resolver.java"
sed -i "s|com.${PACKAGES}.api.poc.TriggerIdentifier|com.${PACKAGES}.configuration.poc.TriggerIdentifier|" "$1/kernel/trigger/TriggerHandler.java"
